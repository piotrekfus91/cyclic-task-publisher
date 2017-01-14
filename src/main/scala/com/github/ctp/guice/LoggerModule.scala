package com.github.ctp.guice

import com.github.ctp.logger.{CtpConsoleLogger, CtpLogger}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class LoggerModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[CtpLogger].to[CtpConsoleLogger]
  }
}
