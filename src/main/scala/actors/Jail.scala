package actors

import akka.actor.{Actor, ActorRef}
import messages.{EndOfDay, GoToJail}

import scala.collection.mutable.Queue

class Jail extends Actor {

  var jailed = new Queue[ActorRef]();

  def receive = {
    case x : GoToJail => jailed += x.actor_ref
    case _: EndOfDay => endOfDay()
    case _      => ()
  }

  def endOfDay(): Unit = {
    for (person <- jailed)  {
      //goto jail
    }
  }

}
