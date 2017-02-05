package com.github.ctp.test

import akka.actor.ActorRef
import com.github.ctp.publisher.PublisherSelector

class TestPublisherSelector(publishers: (String, ActorRef)*) extends PublisherSelector {
  val map = Map(publishers:_*)
  override def get(publisherName: String): ActorRef = map(publisherName)
}
