import akka.actor.Props
import akka.actor.ActorSystem
import actors.DocumentCheck
import akka.actor.PoisonPill
import actors.Person
import messages.StartPerson

object Main {
  def main(args: Array[String]): Unit = {
    
    val system = ActorSystem("mySystem")
    val documentCheck = system.actorOf(Props(new DocumentCheck(0)), "docCheck")
    
    for(x <- 0 to 29){
      val person = system.actorOf(Props(new Person()));
      person ! new StartPerson(documentCheck);
    }
    
    //documentCheck ! PoisonPill
    Thread.sleep(5000)
    system.terminate()
    
  }
}