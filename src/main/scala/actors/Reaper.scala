package actors

import akka.actor.{Actor, ActorRef, ActorSystem}
import messages.{EndOfDay, Increase}

class Reaper(val people: Int, val system: ActorSystem) extends Actor{

  var count = 0

  def receive = {
    case _ : Increase => checkPeople(sender())
    case _ : EndOfDay => println("The system has shut down.");
      system.terminate()
    case _ => ()
  }

  //Checks if all people have arrived at the document check
  def checkPeople(documentCheck: ActorRef): Unit = {
    count += 1
    if (count >= people) {
      documentCheck.tell(new EndOfDay(), self)
      println("The system has begun shutting down.")
    }
  }

}
