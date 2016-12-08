import akka.actor.Props
import akka.actor.ActorSystem
import actors.DocumentCheck
import messages.Document
import akka.actor.PoisonPill

object Main {
  def main(args: Array[String]): Unit = {

    val system = ActorSystem("mySystem")
    val myActor = system.actorOf(Props(new DocumentCheck(null, 0)), "docCheck")
    
    myActor ! new Document(false);
    myActor ! new Document(true);
    myActor ! new Document(true);
    
    
    myActor ! PoisonPill
    system.terminate();
    println("Hello, world!");
  }
}