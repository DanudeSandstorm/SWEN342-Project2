package messages

import akka.actor.ActorRef

class PassFailAbstract(val actor_ref: ActorRef, val pass: Boolean)

class DocPassFail(actor_ref: ActorRef, pass: Boolean) extends PassFailAbstract(actor_ref, pass)