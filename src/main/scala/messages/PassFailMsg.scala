package messages

import akka.actor.ActorRef

abstract class PassFailMsg(val actor_ref: ActorRef, val pass: Boolean)

class DocPassFail(actor_ref: ActorRef, pass: Boolean) extends PassFailMsg(actor_ref, pass)