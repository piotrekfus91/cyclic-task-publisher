package com.github.ctp.publisher

import akka.actor.ActorRef

case class PublisherSequence(publishers: List[ActorRef])
