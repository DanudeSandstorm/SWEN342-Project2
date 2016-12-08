package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages.{EndOfDay, GoToJail}

import scala.collection.mutable.Queue

class Jail(reaper: ActorRef) extends Actor {

  var jailed = new Queue[ActorRef]();

  def receive = {
    case x : GoToJail => jailed += x.actor_ref;
      println(x.actor_ref.path.name + " has been incarcerated to airport jail.")
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  def endOfDay(x: EndOfDay): Unit = {
    for (person <- jailed)  {
      println(person.path.name + " has been transferred to permanent detention.")
      person.tell(PoisonPill, self)
    }
    reaper.tell(x, self)
    self ! PoisonPill
  }

}
