package actors

import akka.actor.{Actor, ActorRef}
import messages.{BagPassFail, BodyPassFail, PassFailMsg, Fly}
import scala.collection.mutable.Queue


class Security extends Actor {

  var passed = new Queue[ActorRef]();

  def receive = {
    case x: BagPassFail => bagChecked(x)
    case x: BodyPassFail => bodyChecked(x)
    case _      => ()
  }

  def bagChecked(x: PassFailMsg) {
    if (x.pass) {
//      if (passed.find(x.actor_ref)) {
//        pass(x.actor_ref)
//      }

      x.actor_ref.tell(self, self) //Sends self to queue
    } else {
      jail(x.actor_ref)
    }
  }

  def bodyChecked(x: PassFailMsg) {
    if (x.pass) {
      x.actor_ref.tell(self, self) //Sends self to queue
    } else {
      jail(x.actor_ref)
    }
  }

  def pass(x: ActorRef): Unit = {
    x.tell(new Fly(),self)
  }

  def jail(x: ActorRef): Unit = {
    //jail//.tell(new GoToJail(x));
  }

}
