package actors

import akka.actor.Actor
import akka.actor.ActorRef

class BodyScan(security: ActorRef) extends Actor {

  def receive = {
    case _      => ()
  }

}
