import akka.actor.Props
import akka.actor.ActorSystem
import actors.DocumentCheck
import messages.Document

object Main {
  def main(args: Array[String]): Unit = {
    
    val system = ActorSystem("mySystem")
    val myActor = system.actorOf(Props(new DocumentCheck(null)), "docCheck")
    
    myActor ! new Document(false);
    myActor ! new Document(true);
    
    println("Hello, world!")
  }
}