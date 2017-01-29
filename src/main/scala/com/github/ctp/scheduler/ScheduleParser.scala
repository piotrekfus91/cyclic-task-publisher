package com.github.ctp.scheduler

trait ScheduleParser {
  def parse(str: String): Either[String, Scheduler]
}
