package actors

import akka.actor.{Actor, ActorRef, PoisonPill}
import messages.{Bag, BagPassFail, EndOfDay}

class BagScan(security: ActorRef) extends Actor{
  println(self.path.name + "has turned on.")

  def receive = {
    case x: Bag => security ! new BagPassFail(sender(), x.isValid)
      println(self.path.name + " scans a bag.")
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  def endOfDay(x: EndOfDay): Unit = {
    println(self.path.name + "has turned off.")
    security.tell(x, self)
    self.tell(PoisonPill, self)
  }

}
