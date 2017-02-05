package com.github.ctp.scheduler

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.github.ctp.config.ConfigReader
import com.github.ctp.config.domain._
import com.github.ctp.publisher.{Publish, PublisherSelector, PublisherSequence}
import com.github.ctp.scheduler.cron.CronScheduleParser
import com.github.ctp.state.{GetLastExecutionTime, LastExecutionTime, NoExecutionYet}
import com.github.ctp.test.TestDateTimeProvider
import com.github.ctp.util.TimeCalculator
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.duration._

class AkkaSchedulerEntryPointActorTest extends TestKit(ActorSystem("test")) with ImplicitSender with FlatSpecLike
    with Matchers with MockFactory with TestDateTimeProvider {

  val configReader = stub[ConfigReader]
  val stateSaver = TestProbe()
  val todoistTaskPublisher = TestProbe()
  val cronScheduleParser = stub[CronScheduleParser]
  val cronScheduler = stub[Scheduler]
  val scheduleParsersFinder = new ScheduleParsersSelector(cronScheduleParser)
  val timeCalculator = new TimeCalculator
  val publisherSelector = new PublisherSelector(todoistTaskPublisher.ref)

  "AkkaSchedulerEntryPoint" should "start single task of single user" in {
    (cronScheduleParser.parse _).when(*).returns(Right(cronScheduler))
    (cronScheduler.getNextTime _).when(*).returns(LocalDateTime.now().plus(100, ChronoUnit.MILLIS))

    val userData = UserData("user", Some(TodoistUser(enabled = true, Some("123456"))))
    val testingTask = Task("description two", "test project", Map("cron" -> "0 0 * * *"), List("todoist"))

    val config = Config(
      Map(("user", UserTasks(
          List(
            Task("description one", "test project", Map(), List()),
            testingTask
          )
        ))
      ),
      Map(("user", userData))
    )

    (configReader.read _).when().returns(config)

    val sut = system.actorOf(Props(new AkkaSchedulerEntryPointActor(
      configReader, stateSaver.ref, scheduleParsersFinder, timeCalculator, dateTimeProvider, publisherSelector)))

    stateSaver.expectMsg(GetLastExecutionTime("user", "description two"))
    sut ! NoExecutionYet("user", "description two")
    todoistTaskPublisher.expectMsg(Publish(userData, testingTask, PublisherSequence(List())))
  }

  it should "start two tasks of single user" in {
    (cronScheduleParser.parse _).when(*).returns(Right(cronScheduler))
    (cronScheduler.getNextTime _).when(*).returns(LocalDateTime.now().plus(100, ChronoUnit.MILLIS))

    val userData = UserData("user", Some(TodoistUser(enabled = true, Some("123456"))))
    val testingTask = Task("description two", "test project", Map("cron" -> "0 0 * * *"), List("todoist"))
    val testingTask2 = Task("description three", "test project", Map("cron" -> "0 0 * * *"), List("todoist"))

    val config = Config(
      Map(("user", UserTasks(
        List(
          Task("description one", "test project", Map(), List()),
          testingTask,
          testingTask2
        )
      ))
      ),
      Map(("user", userData))
    )

    (configReader.read _).when().returns(config)

    val sut = system.actorOf(Props(new AkkaSchedulerEntryPointActor(
      configReader, stateSaver.ref, scheduleParsersFinder, timeCalculator, dateTimeProvider, publisherSelector)))

    stateSaver.expectMsg(GetLastExecutionTime("user", "description two"))
    sut ! NoExecutionYet("user", "description two")
    todoistTaskPublisher.expectMsg(Publish(userData, testingTask, PublisherSequence(List())))

    stateSaver.expectMsg(GetLastExecutionTime("user", "description three"))
    sut ! NoExecutionYet("user", "description three")
    todoistTaskPublisher.expectMsg(Publish(userData, testingTask2, PublisherSequence(List())))
  }

  it should "start two tasks of two users" in {
    dateTimeProvider.update
    (cronScheduleParser.parse _).when(*).returns(Right(cronScheduler))
    (cronScheduler.getNextTime _).when(*).returns(LocalDateTime.now().plus(100, ChronoUnit.MILLIS))

    val userData = UserData("user", Some(TodoistUser(enabled = true, Some("123456"))))
    val user2Data = UserData("user2", Some(TodoistUser(enabled = true, Some("123456"))))

    val testingTask = Task("description two", "test project", Map("cron" -> "0 0 * * *"), List("todoist"))
    val testingTask2 = Task("description three", "test project", Map("cron" -> "0 0 * * *"), List("todoist"))

    val config = Config(
      Map(("user", UserTasks(
        List(
          Task("description one", "test project", Map(), List()),
          testingTask,
          testingTask2
        )
      )), ("user2", UserTasks(
        List(
          Task("description one", "test project", Map(), List()),
          testingTask,
          testingTask2
        )
      ))),
      Map(("user", userData), ("user2", user2Data))
    )

    (configReader.read _).when().returns(config)

    val sut = system.actorOf(Props(new AkkaSchedulerEntryPointActor(
      configReader, stateSaver.ref, scheduleParsersFinder, timeCalculator, dateTimeProvider, publisherSelector)))

    stateSaver.expectMsg(GetLastExecutionTime("user", "description two"))
    sut ! NoExecutionYet("user", "description two")
    todoistTaskPublisher.expectMsg(Publish(userData, testingTask, PublisherSequence(List())))

    stateSaver.expectMsg(GetLastExecutionTime("user", "description three"))
    sut ! NoExecutionYet("user", "description three")
    todoistTaskPublisher.expectMsg(Publish(userData, testingTask2, PublisherSequence(List())))

    stateSaver.expectMsg(GetLastExecutionTime("user2", "description two"))
    sut ! NoExecutionYet("user2", "description two")
    todoistTaskPublisher.expectMsg(Publish(user2Data, testingTask, PublisherSequence(List())))

    stateSaver.expectMsg(GetLastExecutionTime("user2", "description three"))
    sut ! NoExecutionYet("user2", "description three")
    todoistTaskPublisher.expectMsg(Publish(user2Data, testingTask2, PublisherSequence(List())))
  }

  it should "build correct publishers" in {
    dateTimeProvider.update
    (cronScheduleParser.parse _).when(*).returns(Right(cronScheduler))
    (cronScheduler.getNextTime _).when(*).returns(LocalDateTime.now().plus(100, ChronoUnit.MILLIS))

    val userData = UserData("user", Some(TodoistUser(enabled = true, Some("123456"))))
    val testingTask = Task("description two", "test project", Map("cron" -> "0 0 * * *"), List("todoist", "todoist"))

    val config = Config(
      Map(("user", UserTasks(
        List(
          Task("description one", "test project", Map(), List()),
          testingTask
        )
      ))
      ),
      Map(("user", userData))
    )

    (configReader.read _).when().returns(config)

    val sut = system.actorOf(Props(new AkkaSchedulerEntryPointActor(
      configReader, stateSaver.ref, scheduleParsersFinder, timeCalculator, dateTimeProvider, publisherSelector)))

    stateSaver.expectMsg(GetLastExecutionTime("user", "description two"))
    sut ! NoExecutionYet("user", "description two")
    todoistTaskPublisher.expectMsg(Publish(userData, testingTask, PublisherSequence(List(todoistTaskPublisher.ref))))
    todoistTaskPublisher.expectNoMsg(100 millis)
  }

  it should "schedule next execution" in {
    dateTimeProvider.update
    val firstTime = dateTimeProvider.now
    val secondTime = firstTime.plus(200, ChronoUnit.MILLIS)

    (cronScheduleParser.parse _).when(*).returns(Right(cronScheduler))
    (cronScheduler.getNextTime _).when(firstTime).returns(secondTime)

    val userData = UserData("user", Some(TodoistUser(enabled = true, Some("123456"))))
    val testingTask = Task("description two", "test project", Map("cron" -> "0 0 * * *"), List("todoist"))

    val config = Config(
      Map(("user", UserTasks(
        List(
          Task("description one", "test project", Map(), List()),
          testingTask
        )
      ))
      ),
      Map(("user", userData))
    )

    (configReader.read _).when().returns(config)

    val sut = system.actorOf(Props(new AkkaSchedulerEntryPointActor(
      configReader, stateSaver.ref, scheduleParsersFinder, timeCalculator, dateTimeProvider, publisherSelector)))

    stateSaver.expectMsg(GetLastExecutionTime("user", "description two"))
    sut ! LastExecutionTime("user", "description two", firstTime)

    todoistTaskPublisher.expectNoMsg(50 millis)
    todoistTaskPublisher.expectMsg(Publish(userData, testingTask, PublisherSequence(List())))
  }
}
