package actors

import akka.actor.Actor

import scala.util.Random
import messages._

class Person extends Actor {

  val document = new Document(Random.nextDouble() > 0.2)
  val body = new Body(Random.nextDouble() > 0.2)
  val bag = new Bag(Random.nextDouble() > 0.2)


  def receive = {
    case _: StartPerson => sender ! document
    case x: DocPassFail => goToQueue(x)
    case x: WhichBagScan => x.actor_ref.tell(bag, self)
    case x: WhichBodyScan => x.actor_ref.tell(body, self)
    case _: Fly => context.stop(self)
    case _      => ()
  }

  def goToQueue(x: PassFailMsg) {
    if (x.pass) {
        x.actor_ref.tell(self, self) //Sends self to queue
    } else {
      context.stop(self)
    }
  }

}
