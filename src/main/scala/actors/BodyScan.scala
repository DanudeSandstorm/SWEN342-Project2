package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages.{Body, BodyPassFail, BodyScannerReady, EndOfDay}

class BodyScan(security: ActorRef) extends Actor {
  println(self.path.name + "has turned on.")

  var queue: ActorRef = _

  def receive = {
    case x: ActorRef => queue = x
    case x: Body => scanBody(x)
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  def scanBody(body: Body){
    println(self.path.name + " scans a person.")
    security ! new BodyPassFail(sender(), body.isValid)
    queue ! new BodyScannerReady()
  }

  def endOfDay(x: EndOfDay): Unit = {
    println(self.path.name + " has been turned off.")
    security.tell(x, self)
    self.tell(PoisonPill, self)
  }

}
