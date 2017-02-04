package com.github.ctp.guice

import com.github.ctp.util.{DateTimeProvider, DateTimeProviderImpl}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class UtilModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[DateTimeProvider].to[DateTimeProviderImpl]
  }
}
