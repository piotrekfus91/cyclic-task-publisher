package com.github.ctp.guice

import com.github.ctp.publisher.{PublisherSelector, PublisherSelectorImpl}
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class PublisherModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[PublisherSelector].to[PublisherSelectorImpl]
  }
}
