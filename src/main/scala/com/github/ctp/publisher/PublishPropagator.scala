package com.github.ctp.publisher

trait PublishPropagator {
  def propagate(publish: Publish): Unit = {
    publish.publisherSequence.publishers match {
      case List() =>
      case head :: tail =>
        head ! publish.copy(publisherSequence = PublisherSequence(tail))
    }
  }
}