package actors

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable
import messages.{Document, StartPerson}

import scala.collection.immutable.Queue


//class DocumentCheck(people: Queue[ActorRef], n: Int) extends Actor {
class DocumentCheck(n: Int) extends Actor{
  val queues = mutable.MutableList[PersonQueue]()
  var i = 0
  for (i <- 0 until n) {
    queues += new PersonQueue()
  }
  i = 0

//  Starts all the people
//  for (person <- people) person ! new StartPerson(self)

  def checkDocument(d: Document){
    if(d.isValid){
      println("Valid Document");
//      sender() ! new DocPassFail(nextQueue, true);
    }
    else{
      println("Invalid Document");
//      sender() ! new DocPassFail(null, false);
    }
  }
  
  def receive = {
    case x : Document => checkDocument(x);
    case _ => ;
  }


  def nextQueue = {
    if (i >= n) i = 0
    val queue = queues.get(i)
    i += 1
    queue //return
  }
}