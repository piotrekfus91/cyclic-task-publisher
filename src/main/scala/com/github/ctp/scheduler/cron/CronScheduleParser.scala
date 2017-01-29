package com.github.ctp.scheduler.cron

import com.cronutils.model.CronType
import com.cronutils.model.definition.CronDefinitionBuilder
import com.cronutils.parser.CronParser
import com.github.ctp.scheduler.{ScheduleParser, Scheduler}

import scala.util.control.NonFatal

class CronScheduleParser extends ScheduleParser{
  override def parse(str: String): Either[String, Scheduler] = {
    val cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX)
    val cronParser = new CronParser(cronDefinition)
    try {
      val cron = cronParser.parse(str)
      cron.validate()
      Right(new CronScheduler(cron))
    } catch {
      case NonFatal(e) => Left(e.getMessage)
    }
  }
}
