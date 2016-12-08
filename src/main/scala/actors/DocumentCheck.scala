package actors

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable
import messages.{Document, StartPerson,DocPassFail}
import scala.collection.immutable.Queue


class DocumentCheck(queues: mutable.MutableList[ActorRef]) extends Actor{
  

  def checkDocument(d: Document){
    if(d.isValid){
      println("Valid Document");
      sender() ! new DocPassFail(nextQueue, true);
    }
    else{
      println("Invalid Document");
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