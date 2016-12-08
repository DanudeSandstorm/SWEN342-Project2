package actors

import akka.actor.Actor

import scala.util.Random
import messages._
import akka.actor.PoisonPill

class Person extends Actor {

  val document = new Document(Random.nextDouble() > 0.2)
  val body = new Body(Random.nextDouble() > 0.2)
  val bag = new Bag(Random.nextDouble() > 0.2)


  def receive = {
    case x: StartPerson => printf(self.path.name + ": Giving Document to DocCheck\n"); x.actor_ref.tell(document, self)
    case x: DocPassFail => goToQueue(x)
    case x: WhichBagScan => x.actor_ref.tell(bag, self)
    case x: WhichBodyScan => x.actor_ref.tell(body, self)
    case _: Fly => ()
    case _      => ()
  }

  def goToQueue(x: DocPassFail) {
    if (x.pass) {
        printf(self.path.name + ": Entering " + x.actor_ref.path.name + "\n");
        x.actor_ref ! this //Sends self to queue
    } else {
      context.stop(self)
    }
  }

}
