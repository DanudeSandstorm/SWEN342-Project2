package actors

import akka.actor.Actor
import messages.Document
import messages.PassFailMsg

class DocumentCheck(queue: Actor) extends Actor{
  
  def checkDocument(d: Document){
    if(d.isValid){
      sender() ! new PassFailMsg(queue, true);
    }
    else{
      sender() ! new PassFailMsg(null, false);
    }
  }
  
  def receive = {
    case x : Document => checkDocument(x);
    case _ => ;
  }
  
}