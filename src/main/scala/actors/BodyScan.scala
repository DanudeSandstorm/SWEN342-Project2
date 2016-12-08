package actors

import akka.actor.Actor
import akka.actor.ActorRef
import messages.Body
import messages.BodyScannerReady
import messages.BodyPassFail

class BodyScan(security: ActorRef) extends Actor {

  def scanBody(body: Body){
    security ! new BodyPassFail(sender(), body.isValid)
    sender() ! new BodyScannerReady()
  }
  
  def receive = {
    case x: Body => scanBody(x)
    case _      => ()
  }

}
