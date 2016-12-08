package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages.{Bag, BagPassFail, EndOfDay}

class BagScan(security: ActorRef) extends Actor{

  def receive = {
    case x: Bag => security ! new BagPassFail(sender(), x.isValid)
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  def endOfDay(x: EndOfDay): Unit = {
    security.tell(x, self)
    self.tell(PoisonPill, self)
  }

}
