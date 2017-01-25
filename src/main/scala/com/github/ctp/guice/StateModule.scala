package com.github.ctp.guice

import akka.actor.{Actor, ActorRef, ActorSystem}
import com.github.ctp.state.{StateSaver, StateSaverActor, StateSerializer, StateSerializerActor}
import com.google.inject.{AbstractModule, Inject, Provides}
import net.codingwell.scalaguice.ScalaModule

class StateModule extends AbstractModule with ScalaModule with GuiceAkkaActorRefProvider {
  override def configure(): Unit = {
    bind[Actor].annotatedWithName(ActorName[StateSaver]).to[StateSaverActor]
    bind[Actor].annotatedWithName(ActorName[StateSerializer]).to[StateSerializerActor]
  }

  @Provides
  @StateSaver
  def stateSaver(@Inject() system: ActorSystem): ActorRef = provideActorRef(system, ActorName[StateSaverActor])

  @Provides
  @StateSerializer
  def stateSerializer(@Inject() system: ActorSystem): ActorRef = provideActorRef(system, ActorName[StateSerializerActor])
}
