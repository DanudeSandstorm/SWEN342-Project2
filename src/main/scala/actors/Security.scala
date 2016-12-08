package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages._

import scala.collection.mutable.Queue


class Security(jail: ActorRef) extends Actor {

  var passed = new Queue[ActorRef]();

  def receive = {
    case x: BagPassFail => scannerResult(x)
    case x: BodyPassFail => scannerResult(x)
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  def scannerResult(x: PassFailMsg) {
    if (x.pass) {
      if (passed.indexOf(x.actor_ref) >= 0) {
        x.actor_ref.tell(new Fly(), self)
      }
      else {
        passed += x.actor_ref
      }
    }
    else {
      jail.tell(new GoToJail(x.actor_ref), self)
    }
  }

  var close = 0
  def endOfDay(x: EndOfDay): Unit = {
    close += 1
    if (close >= 2) {
      jail ! x
      self ! PoisonPill
    }
  }

}
