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
    case x: DocPassFail => returnDocument(x)
    case _: Fly => goFly()
    case _      => ()
  }

  def returnDocument(x: PassFailAbstract) {
    if (x.pass) {
        x.actor_ref.tell(self, self)
    } else {
      context.stop(self)
    }
  }

  def goFly() {
    context.stop(self)
  }

}
