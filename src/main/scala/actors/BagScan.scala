package actors

import akka.actor.Actor
import akka.actor.ActorRef

class BagScan(security: ActorRef) extends Actor{

  def receive = {
    case _      => ()
  }

}
