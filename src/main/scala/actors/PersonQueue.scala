package actors

import akka.actor.{Actor, ActorRef, PoisonPill}

import scala.collection.mutable
import messages.{BodyScannerReady, EndOfDay, WhichBagScan, WhichBodyScan}

class PersonQueue(bagScan: ActorRef, bodyScan: ActorRef) extends Actor {

  val personQueue = mutable.Queue[ActorRef]()

  def addToQueue() {
    personQueue.enqueue(sender())

    println(self.path.name + " tells " + sender().path.name + " to put their bag in the bag scanner.")
    sender() ! new WhichBagScan(bagScan)
    if(empty){
      sendNextPersonThroughBodyScanner()
    }
  }

  var empty = true
  def sendNextPersonThroughBodyScanner(){
    if (personQueue.nonEmpty) {
      empty = false
      val nextPerson = personQueue.dequeue()
      println(self.path.name + " signals " + nextPerson.path.name + " to step into the body scanner.")
      nextPerson ! new WhichBodyScan(bodyScan)
    }
    else {
      println("DAD")
      if (shutdown) {
        endOfDay(new EndOfDay())
      }
    }
  }
  
  def receive = {
    case _: ActorRef => addToQueue()
    case _: BodyScannerReady => empty = true; sendNextPersonThroughBodyScanner()
    case x: EndOfDay => shutdown = true; endOfDay(x)
    case _      => ()
  }

  var shutdown = false
  def endOfDay(x: EndOfDay): Unit = {
    if (empty) {
      println(self.path.name + " signals " + bodyScan.path.name + " and " + bodyScan.path.name + " to shut down.")
      bagScan.tell(x, self)
      bodyScan.tell(x, self)
      self.tell(PoisonPill, self)
    }
  }

}
