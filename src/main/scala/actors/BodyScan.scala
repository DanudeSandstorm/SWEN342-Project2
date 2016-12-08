package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages.{Body, BodyPassFail, BodyScannerReady, EndOfDay}

class BodyScan(security: ActorRef) extends Actor {

  def scanBody(body: Body){
    security ! new BodyPassFail(sender(), body.isValid)
    sender() ! new BodyScannerReady()
  }
  
  def receive = {
    case x: Body => scanBody(x)
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  def endOfDay(x: EndOfDay): Unit = {
    security.tell(x, self)
    self.tell(PoisonPill, self)
  }

}
