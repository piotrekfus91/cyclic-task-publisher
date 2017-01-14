package com.github.ctp.guice

import akka.actor.{Actor, ActorRef, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider, IndirectActorProducer, Props}
import com.google.inject.name.Names
import com.google.inject._
import net.codingwell.scalaguice.ScalaModule

class AkkaModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[ActorSystem].toProvider[ActorSystemProvider].asEagerSingleton()
  }
}

class ActorSystemProvider @Inject() (private val injector: Injector) extends Provider[ActorSystem] {
  override def get(): ActorSystem = {
    val system = ActorSystem("ctpSystem")
    GuiceAkkaExtension(system).initialize(injector)
    system
  }
}

class GuiceAkkaExtensionImpl extends Extension {
  private var injector: Injector = _

  def initialize(injector: Injector) {
    this.injector = injector
  }

  def props(actorName: String) = Props(classOf[GuiceActorProducer], injector, actorName)
}

object GuiceAkkaExtension extends ExtensionId[GuiceAkkaExtensionImpl] with ExtensionIdProvider {
  override def lookup() = GuiceAkkaExtension
  override def createExtension(system: ExtendedActorSystem) = new GuiceAkkaExtensionImpl
  override def get(system: ActorSystem): GuiceAkkaExtensionImpl = super.get(system)

}

trait NamedActor {
  def actorName: String = getClass.getSimpleName
}

trait GuiceAkkaActorRefProvider {
  def propsFor(system: ActorSystem, name: String) = GuiceAkkaExtension(system).props(name)
  def provideActorRef(system: ActorSystem, name: String): ActorRef = system.actorOf(propsFor(system, name))
}

class GuiceActorProducer(val injector: Injector, val actorName: String) extends IndirectActorProducer {
  override def actorClass = classOf[Actor]
  override def produce() = injector.getBinding(Key.get(classOf[Actor], Names.named(actorName))).getProvider.get()
}