package com.github.ctp.scheduler

import java.time.LocalDateTime

import akka.actor.{Actor, ActorRef}
import com.github.ctp.config.ConfigReader
import com.github.ctp.config.domain.{Config, Task, UserData}
import com.github.ctp.publisher.{Publish, PublisherSelector, PublisherSequence}
import com.github.ctp.state.{GetLastExecutionTime, LastExecutionTime, NoExecutionYet, StateSaver}
import com.github.ctp.util.{DateTimeProvider, TimeCalculator}
import com.google.inject.BindingAnnotation
import com.typesafe.scalalogging.LazyLogging

import scala.annotation.StaticAnnotation
import scala.collection.immutable.Iterable
import scala.concurrent.ExecutionContext.Implicits.global

class AkkaSchedulerEntryPointActor(configReader: ConfigReader, @StateSaver stateSaver: ActorRef,
                                   scheduleParsersFinder: ScheduleParsersSelector, timeCalculator: TimeCalculator,
                                   dateTimeProvider: DateTimeProvider,
                                   publisherSelector: PublisherSelector) extends Actor with LazyLogging {
  private val config = configReader.read()

  override def preStart(): Unit = {
    super.preStart()
    tryRunAllTasks(config)
  }

  private def tryRunAllTasks(config: Config) = {
    for {
      (userName, tasks) <- config.allTasks
      task <- tasks.tasks
      if task.schedule.nonEmpty
      if task.publishers.nonEmpty
    } yield stateSaver ! GetLastExecutionTime(userName, task.description)
  }

  override def receive: Receive = {
    case NoExecutionYet(user, description) => scheduleFirstExecution(user, description)
    case LastExecutionTime(user, description, lastExecutionTime) => scheduleNextExecution(user, description, lastExecutionTime)
  }

  private def scheduleFirstExecution(userName: String, description: String) = {
    scheduleFromConfig(userName, description, None)
  }

  private def scheduleNextExecution(user: String, description: String, lastExecutionTime: LocalDateTime) = {
    scheduleFromConfig(user, description, Some(lastExecutionTime))
  }

  private def scheduleFromConfig(userName: String, description: String, last: Option[LocalDateTime]) = {
    logger.info(s"scheduling task $description for user $userName to date $last")
    val user = config.users(userName)
    val maybeTask = config.allTasks(userName).tasks.find(task => task.description == description)
    maybeTask.foreach(task => {
      val parserResults: Iterable[Either[String, Scheduler]] = createParseResults(task)
      logParsingErrors(parserResults)
      scheduleTasks(parserResults, user, task, last)
    })
  }

  private def scheduleTasks(parserResults: Iterable[Either[String, Scheduler]], user: UserData, task: Task,
                            last: Option[LocalDateTime]) = {
    parserResults.filter(either => either.isRight).map(_.right.get).foreach(scheduler => {
      val nextTime = scheduler.getNextTime(last.getOrElse(dateTimeProvider.now))
      val publishers = task.publishers.map(publisherSelector.get)
      publishers match {
        case head :: tail =>
          context.system.scheduler.scheduleOnce(
            delay = timeCalculator.calculateDuration(dateTimeProvider.now, nextTime),
            receiver = head,
            message = Publish(user, task, PublisherSequence(tail))
          )
      }
    })
  }

  private def createParseResults(task: Task) = {
    val parserResults = for {
      (scheduler, scheduleDescription) <- task.schedule
      parser = scheduleParsersFinder.get(scheduler)
    } yield parser.parse(scheduleDescription)
    parserResults
  }

  private def logParsingErrors(parserResults: Iterable[Either[String, Scheduler]]) = {
    parserResults.filter(either => either.isLeft).map(_.left.get).foreach(logger.warn(_))
  }
}

@BindingAnnotation
class AkkaSchedulerEntryPoint extends StaticAnnotation