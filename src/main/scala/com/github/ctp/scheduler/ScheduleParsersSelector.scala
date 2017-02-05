package com.github.ctp.scheduler

import com.github.ctp.scheduler.cron.CronScheduleParser

class ScheduleParsersSelector(cronScheduleParser: CronScheduleParser) {
  private val parsers = Map[String, ScheduleParser](
    "cron" -> cronScheduleParser
  )

  def get(parser: String) = parsers(parser)
}
