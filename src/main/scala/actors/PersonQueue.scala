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

    println(self.path.name + ": telling " + sender().path.name + " to put their bag in the scanner");
    sender() ! new WhichBagScan(bagScan)
    if(personQueue.length == 1){
      sendNextPersonThroughBodyScanner()
    }
  }
  
  def sendNextPersonThroughBodyScanner(){
    val nextPerson = personQueue.dequeue()
    println(self.path.name + ": telling " + nextPerson.path.name + " to step into the bodyscanner");
    nextPerson ! new WhichBodyScan(bodyScan)
  }
  
  def receive = {
    case x: Person => addToQueue(x) 
    case _: BodyScannerReady => sendNextPersonThroughBodyScanner()
    case _      => ()
  }

}
