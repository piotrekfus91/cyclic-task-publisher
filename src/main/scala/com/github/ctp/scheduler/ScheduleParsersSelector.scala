package com.github.ctp.scheduler

import com.github.ctp.scheduler.cron.CronScheduleParser

trait ScheduleParsersSelector {
  def get(parser: String): ScheduleParser
}

class ScheduleParsersSelectorImpl(cronScheduleParser: CronScheduleParser) extends ScheduleParsersSelector {
  private val parsers = Map[String, ScheduleParser](
    "cron" -> cronScheduleParser
  )

  override def get(parser: String) = parsers(parser)
}
