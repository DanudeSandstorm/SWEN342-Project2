package actors

import akka.actor.Actor
import akka.actor.ActorRef
import messages.BagPassFail
import messages.Bag

class BagScan(security: ActorRef) extends Actor{

  def receive = {
    case x: Bag => security ! new BagPassFail(sender(), x.isValid)
    case _      => ()
  }

}
