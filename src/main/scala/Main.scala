import akka.actor.Props
import akka.actor.ActorSystem
import actors._
import messages.StartPerson

import scala.collection.mutable
import akka.actor.ActorRef

object Main {
  val numberOfQueues = 3
  val numberOfPeople = 10
  
  def main(args: Array[String]): Unit = {
    
    val system = ActorSystem("System")

    //Reaper ends the system when done
    val reaper = system.actorOf(Props(new Reaper(numberOfPeople, system)))

    val jail = system.actorOf(Props(new Jail(numberOfQueues, reaper)))
    
    val queues = mutable.MutableList[ActorRef]()

    for (i <- 0 until numberOfQueues) {
      val security = system.actorOf(Props(new Security(jail)), "SecurityStation" + i)
      val bagScanner = system.actorOf(Props(new BagScan(security)), "BagScanner" + i)
      val bodyScanner = system.actorOf(Props(new BodyScan(security)), "BodyScanner" + i)
      queues += system.actorOf(Props(new PersonQueue(bagScanner, bodyScanner)), "Queue" + i)
    }
    
    val documentCheck = system.actorOf(Props(new DocumentCheck(queues, reaper)), "Document_Checker")

    for(i <- 0 until numberOfPeople){
      val person = system.actorOf(Props(new Person()), "Person" + i)
      person ! new StartPerson(documentCheck)
      
    }

  }
}