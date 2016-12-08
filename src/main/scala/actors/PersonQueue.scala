package actors

import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.mutable
import messages.WhichBagScan
import messages.WhichBodyScan
import messages.BodyScannerReady

class PersonQueue(bagScan: ActorRef, bodyScan: ActorRef) extends Actor {

  val personQueue = mutable.Queue[ActorRef]()
  
  def addToQueue(person: Person){
    personQueue.enqueue(sender());
    printf("Person " + person.toString() + " Queued!\n");
    
    sender() ! new WhichBagScan(bagScan)
    if(personQueue.length == 1){
      sendNextPersonThroughBodyScanner()
    }
  }
  
  def sendNextPersonThroughBodyScanner(){
    personQueue.dequeue() ! new WhichBodyScan(bodyScan)
  }
  
  def receive = {
    case x: Person => addToQueue(x) 
    case _: BodyScannerReady => sendNextPersonThroughBodyScanner()
    case _      => ()
  }

}
