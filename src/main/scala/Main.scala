import akka.actor.Props
import akka.actor.ActorSystem
import actors._
import messages.Document
import akka.actor.PoisonPill
import messages.StartPerson

import scala.collection.mutable
import akka.actor.ActorRef

object Main {
  val numberOfQueues = 3;
  val numberOfPeople = 10
  
  def main(args: Array[String]): Unit = {
    
    val system = ActorSystem("mySystem")

    //Ends the system when done
    val reaper = system.actorOf(Props(new Reaper(numberOfPeople, system)))

    val jail = system.actorOf(Props(new Jail(reaper)));

    val security = system.actorOf(Props(new Security(jail)));
    
    val queues = mutable.MutableList[ActorRef]()
    for (i <- 0 until numberOfQueues) {
      val bagScanner = system.actorOf(Props(new BagScan(security)));
      val bodyScanner = system.actorOf(Props(new BodyScan(security)));
      queues += system.actorOf(Props(new PersonQueue(bagScanner, bodyScanner)), "Queue"+i);
    }
    
    val documentCheck = system.actorOf(Props(new DocumentCheck(queues, reaper)), "docCheck")
    for(x <- 0 until numberOfPeople){
      
      val person = system.actorOf(Props(new Person()), "Person".concat(x.toString()) );
      person ! new StartPerson(documentCheck);
      
    }

  }
}