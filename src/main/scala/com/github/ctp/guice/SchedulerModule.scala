package com.github.ctp.guice

import com.github.ctp.scheduler.{ScheduleParsersSelector, ScheduleParsersSelectorImpl}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class SchedulerModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ScheduleParsersSelector].to[ScheduleParsersSelectorImpl]
  }
}
