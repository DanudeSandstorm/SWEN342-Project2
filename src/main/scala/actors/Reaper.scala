package actors

import akka.actor.{Actor, ActorRef, ActorSystem}
import messages.{EndOfDay, Increase}

class Reaper(val people: Int, val system: ActorSystem) extends Actor{

  var count = 0

  def receive = {
    case _ : Increase => checkPeople(sender())
    case _ : EndOfDay => system.terminate()
    case _ => ()
  }

  def checkPeople(x: ActorRef): Unit = {
    count += 1
    if (count >= people) x.tell(new EndOfDay(), self)
  }

}
