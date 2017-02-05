package com.github.ctp.publisher

import akka.actor.ActorRef
import com.github.ctp.publisher.todoist.TodoistTaskPublisher

trait PublisherSelector {
  def get(publisherName: String): ActorRef
}

class PublisherSelectorImpl(@TodoistTaskPublisher todoistTaskPublisher: ActorRef) extends PublisherSelector {
  private val publishers = Map[String, ActorRef](
    "todoist" -> todoistTaskPublisher
  )

  def get(publisherName: String) = publishers(publisherName)
}
