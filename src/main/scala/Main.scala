import akka.actor.Props
import akka.actor.ActorSystem
import actors.DocumentCheck
import messages.Document
import akka.actor.PoisonPill
import actors.Person
import messages.StartPerson
import actors.{PersonQueue, BagScan, BodyScan, Jail, Security}
import scala.collection.mutable
import akka.actor.ActorRef

object Main {
  val numberOfQueues = 3;
  
  def main(args: Array[String]): Unit = {
    
    val system = ActorSystem("mySystem")
    
    val jail = system.actorOf(Props(new Jail()));
    val security = system.actorOf(Props(new Security(jail)));
    
    val queues = mutable.MutableList[ActorRef]()
    for (i <- 0 until numberOfQueues) {
      val bagScanner = system.actorOf(Props(new BagScan(security)));
      val bodyScanner = system.actorOf(Props(new BodyScan(security)));
      queues += system.actorOf(Props(new PersonQueue(bagScanner, bodyScanner)), "Queue"+i);
    }
    
    val documentCheck = system.actorOf(Props(new DocumentCheck(queues)), "docCheck")
    for(x <- 0 to 10){
      
      val person = system.actorOf(Props(new Person()), "Person".concat(x.toString()) );
      person ! new StartPerson(documentCheck);
      
    }
    
    Thread.sleep(1000)
    documentCheck ! PoisonPill
    Thread.sleep(1000)
    system.terminate()
    
  }
}