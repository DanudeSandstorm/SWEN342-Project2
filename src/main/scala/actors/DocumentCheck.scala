package actors

import akka.actor.{Actor, ActorRef, PoisonPill}

import scala.collection.mutable
import messages._


class DocumentCheck(queues: mutable.MutableList[ActorRef], reaper: ActorRef) extends Actor{

  
  def receive = {
    case x : Document => checkDocument(x);
    case x: EndOfDay => endOfDay(x);
    case _ => ;
  }

  def checkDocument(d: Document){
    if(d.isValid){
      val queue = nextQueue
      println(self.path.name + " tells " + sender().path.name + " to wait in " + queue.path.name)
      sender() ! new DocPassFail(queue, true)
    }
    else {
      println(self.path.name + " tells passenger they have an invalid document")
      sender() ! new DocPassFail(null, false)
    }

    reaper.tell(new Increase, self)
  }

  var i = 0
  def nextQueue: ActorRef = {
    if (i >= queues.length){
      i = 0
    }
    val queue = queues.get(i)
    i += 1
    queue.get
  }

  def endOfDay(x: EndOfDay): Unit = {
    for (queue <- queues) {
      println(self.path.name + " tells " + queue.path.name + " to begin shutting down.")
      queue.tell(new EndOfDay(), self)
    }
    self ! PoisonPill
  }

}