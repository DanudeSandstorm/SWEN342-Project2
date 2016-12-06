package messages

import akka.actor.Actor;

class PassFailMsg(val person: Actor, val pass: Boolean)