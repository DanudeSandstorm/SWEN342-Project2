package actors

import akka.actor.Actor
import scala.util.Random
import messages.{Document, Bag, Body}

class Person extends Actor {

  val document = new Document(Random.nextDouble() > 0.2)
  val body = new Body(Random.nextDouble() > 0.2)
  val bag = new Bag(Random.nextDouble() > 0.2)

  def receive = {
    case _      => ()
  }

}
