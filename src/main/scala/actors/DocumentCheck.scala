package actors

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable
import messages.{DocPassFail, Document, Increase, StartPerson}


class DocumentCheck(queues: mutable.MutableList[ActorRef], reaper: ActorRef) extends Actor{
  

  def checkDocument(d: Document){
    reaper.tell(new Increase, self)

    if(d.isValid){
      val queue = nextQueue;
      println(self.path.name + ": Telling " + sender().path.name + " to wait in " + queue.path.name);
      sender() ! new DocPassFail(queue, true);
    }
    else{
      println(self.path.name + ": Telling passenger they have an invalid document");
      sender() ! new DocPassFail(null, false);
    }
  }
  
  def receive = {
    case x : Document => checkDocument(x);
    case _ => ;
  }

  var n = queues.length
  var i = 0
  
  def nextQueue: ActorRef = {
    if (i >= n){
      i = 0
    }
    val queue = queues.get(i)
    i += 1
    return queue.get
  }
}