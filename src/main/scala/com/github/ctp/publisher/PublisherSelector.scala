package com.github.ctp.publisher

import akka.actor.ActorRef
import com.github.ctp.publisher.todoist.TodoistTaskPublisher

class PublisherSelector(@TodoistTaskPublisher todoistTaskPublisher: ActorRef) {
  private val publishers = Map[String, ActorRef](
    "todoist" -> todoistTaskPublisher
  )

  def get(publisherName: String) = publishers(publisherName)
}
