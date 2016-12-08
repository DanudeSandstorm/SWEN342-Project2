package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages._

import scala.collection.mutable


class Security(jail: ActorRef) extends Actor {

  var scans = mutable.HashMap[ActorRef, Boolean]()

  def receive = {
    case x: BagPassFail => scannerResult(x)
    case x: BodyPassFail => scannerResult(x)
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  def scannerResult(x: PassFailMsg) {
    if (scans.get(x.actor_ref).isEmpty) {
      scans.put(x.actor_ref, x.pass)
    }
    else {
      //If either didn't pass
      if (!(scans(x.actor_ref) && x.pass)) {
        jail ! new GoToJail(x.actor_ref)
      }
      else {
        x.actor_ref ! new Fly()
      }
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
