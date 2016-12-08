package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages.{Body, BodyPassFail, BodyScannerReady, EndOfDay}

class BodyScan(security: ActorRef) extends Actor {
  println(self.path.name + "has turned on.")

  def receive = {
    case x: Body => scanBody(x)
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  def scanBody(body: Body){
    println(self.path.name + " scans a person.")
    security ! new BodyPassFail(sender(), body.isValid)
    sender() ! new BodyScannerReady()
  }

  def endOfDay(x: EndOfDay): Unit = {
    println(self.path.name + "has turned off.")
    security.tell(x, self)
    self.tell(PoisonPill, self)
  }

}
