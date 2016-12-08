package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages.{EndOfDay, GoToJail}

import scala.collection.mutable.Queue

class Jail(reaper: ActorRef) extends Actor {

  var jailed = new Queue[ActorRef]();

  def receive = {
    case x : GoToJail => jailed += x.actor_ref
    case _: EndOfDay => endOfDay()
    case _      => ()
  }

  def endOfDay(): Unit = {
    for (person <- jailed)  {
      //goto jail
      person.tell(PoisonPill, self)
    }
    reaper.tell(new EndOfDay(), self)
  }

}
