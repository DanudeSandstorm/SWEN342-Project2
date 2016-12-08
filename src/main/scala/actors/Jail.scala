package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages.{EndOfDay, GoToJail}
import scala.collection.mutable.Queue

class Jail(queues: Int, reaper: ActorRef) extends Actor {

  var jailed = new Queue[ActorRef]();

  def receive = {
    case x : GoToJail => jailed += x.actor_ref;
      println(x.actor_ref.path.name + " has been incarcerated to airport jail.")
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  var count = 0
  def endOfDay(x: EndOfDay): Unit = {
    count += 1
    if (count >= queues) {
      for (person <- jailed) {
        println(person.path.name + " has been transferred to permanent detention.")
        person.tell(PoisonPill, self)
      }
      reaper.tell(x, self)
      self ! PoisonPill
    }
  }

}
