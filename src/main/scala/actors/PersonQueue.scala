package actors

import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.immutable

class PersonQueue extends Actor {

  val personQueue = immutable.Queue[Actor]()
  
  def addToQueue(person: ActorRef){
    printf("Person " + person.toString() + " Queued!\n");
  }
  
  def receive = {
    case x: ActorRef => addToQueue(x) 
    case _      => ()
  }

}
