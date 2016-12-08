package actors

import akka.actor.{Actor, ActorRef}
import messages.{BagPassFail, BodyPassFail, PassFailMsg, Fly}
import scala.collection.mutable.Queue


class Security extends Actor {

  var passed = new Queue[ActorRef]();

  def receive = {
    case x: BagPassFail => scannerResult(x)
    case x: BodyPassFail => scannerResult(x)
    case _      => ()
  }

  def scannerResult(x: PassFailMsg) {
    if (x.pass) {
      //      if (passed.find(x.actor_ref)) {
      //         x.tell(new Fly(),self)
      //      }
      // else {
//          passed += x.actor_ref
//      }
    }
    else {
      //jail//.tell(new GoToJail(x));
    }
  }

}
