package messages

import akka.actor.ActorRef

abstract class ActorRefMsg(val actor_ref: ActorRef)

class StartPerson(actor_ref: ActorRef) extends ActorRefMsg(actor_ref)

class WhichBagScan(actor_ref: ActorRef) extends ActorRefMsg(actor_ref)

class WhichBodyScan(actor_ref: ActorRef) extends ActorRefMsg(actor_ref)

class GoToJail(actor_ref: ActorRef) extends ActorRefMsg(actor_ref)