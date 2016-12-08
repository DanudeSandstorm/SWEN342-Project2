import akka.actor.Props
import akka.actor.ActorSystem
import actors.DocumentCheck
import messages.Document
import akka.actor.PoisonPill
import actors.Person
import messages.StartPerson
import actors.PersonQueue

object Main {
  def main(args: Array[String]): Unit = {
    
    val system = ActorSystem("mySystem")
    val documentCheck = system.actorOf(Props(new DocumentCheck(0)), "docCheck")
    for(x <- 0 to 100){
      
      val person = system.actorOf(Props(new Person()), "Person".concat(x.toString()) );
      person ! new StartPerson(documentCheck);
      
    }
    
    Thread.sleep(100)
    documentCheck ! PoisonPill
    Thread.sleep(1900)
    system.terminate()
    
  }
}