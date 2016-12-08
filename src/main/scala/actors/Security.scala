package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages._

import scala.collection.mutable


class Security(jail: ActorRef) extends Actor {

  var scans = mutable.HashMap[ActorRef, Boolean]()

  def receive = {
    case x: BagPassFail => scannerResult(x)
      println(self.path.name + " sees result of " + x.actor_ref.path.name + "'s bag scan.")
    case x: BodyPassFail => scannerResult(x)
      println(self.path.name + " sees result of " + x.actor_ref.path.name + "'s body scan.")
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
        println(x.actor_ref.path.name + " didn't pass a scan and is sent to jail.")
        jail ! new GoToJail(x.actor_ref)
      }
      else {
        println(self.path.name + " tells " + x.actor_ref.path.name + " has passed.")
        x.actor_ref ! new Fly()
      }
    }
  }

  var close = 0
  def endOfDay(x: EndOfDay): Unit = {
    close += 1
    if (close >= 2) {
      println(self.path.name + " is closed.")
      jail ! x
      self ! PoisonPill
    }
  }

}
