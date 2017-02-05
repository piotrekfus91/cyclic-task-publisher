package com.github.ctp.publisher

import akka.actor.Actor

trait Publisher extends Actor with PublishPropagator {

}
