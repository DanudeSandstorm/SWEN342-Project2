package actors

import akka.actor.Actor
import akka.actor.ActorRef
import messages.{Bag, BagPassFail, EndOfDay}

class BagScan(security: ActorRef) extends Actor{

  def receive = {
    case x: Bag => security ! new BagPassFail(sender(), x.isValid)
    case x: EndOfDay => security.tell(x, self)
    case _      => ()
  }

}
