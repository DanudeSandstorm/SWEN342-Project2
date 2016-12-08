package actors

import akka.actor.{Actor, ActorRef, PoisonPill}

import scala.collection.mutable
import messages.{BodyScannerReady, EndOfDay, WhichBagScan, WhichBodyScan}

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
    if (personQueue.nonEmpty) {
      val nextPerson = personQueue.dequeue()
      println(self.path.name + ": telling " + nextPerson.path.name + " to step into the bodyscanner");
      nextPerson ! new WhichBodyScan(bodyScan)
    }
  }
  
  def receive = {
    case x: Person => addToQueue(x) 
    case _: BodyScannerReady => sendNextPersonThroughBodyScanner()
    case x: EndOfDay => endOfDay(x)
    case _      => ()
  }

  def endOfDay(x: EndOfDay): Unit = {
    bagScan.tell(x, self)
    //bodyScan.tell(x, self)
    self.tell(PoisonPill, self)
  }


}
