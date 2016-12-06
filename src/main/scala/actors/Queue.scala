package actors

import akka.actor.Actor
import scala.collection.immutable

class Queue extends Actor {

  val personQueue = immutable.Queue[Actor]()
  
  def addToQueue(person: Actor){
    
  }
  
  def receive = {
    case x: Actor => 
    case _      => ()
  }

}
