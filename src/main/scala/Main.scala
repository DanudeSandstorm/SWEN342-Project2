import akka.actor.Props
import akka.actor.ActorSystem
import actors.DocumentCheck
import messages.Document
import akka.actor.PoisonPill
import actors.Person
import messages.StartPerson
import actors.{PersonQueue, BagScan, BodyScan}
import scala.collection.mutable
import akka.actor.ActorRef

object Main {
  val numberOfQueues = 10;
  
  def main(args: Array[String]): Unit = {
    
    val system = ActorSystem("mySystem")
    
    val queues = mutable.MutableList[ActorRef]()
    for (i <- 0 until numberOfQueues) {
      val bagScanner = system.actorOf(Props(new BagScan()));
      val bodyScanner = system.actorOf(Props(new BodyScan()));
      queues += system.actorOf(Props(new PersonQueue(bagScanner, bodyScanner)));
    }
    
    val documentCheck = system.actorOf(Props(new DocumentCheck(queues)), "docCheck")
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