package com.github.ctp.test

import com.github.ctp.scheduler.{ScheduleParser, ScheduleParsersSelector}

class TestScheduleParsersSelector(parsers: (String, ScheduleParser)*) extends ScheduleParsersSelector {
  val map = Map(parsers:_*)

  override def get(parser: String): ScheduleParser = map(parser)
}
