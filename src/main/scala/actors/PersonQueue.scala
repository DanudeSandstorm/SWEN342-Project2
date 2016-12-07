package actors

import akka.actor.Actor
import scala.collection.immutable

class PersonQueue extends Actor {

  val personQueue = immutable.Queue[Actor]()
  
  def addToQueue(person: Actor){
    
  }
  
  def receive = {

    case _      => ()
  }

}
