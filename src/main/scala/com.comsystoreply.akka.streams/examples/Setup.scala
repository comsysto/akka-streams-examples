package com.comsystoreply.akka.streams.examples

import java.util.UUID
import java.util.concurrent.atomic.{AtomicInteger}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.util.Random

trait Setup {

  implicit val actorSystem: ActorSystem = ActorSystem("akka-streams-examples")
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher

  val random: Random = Random

  private val min = 1
  private val max = 20

  def randomKey(): Int = min + random.nextInt( (max - min) + 1 )

  def uuid() = UUID.randomUUID().toString

  private val sequence = new AtomicInteger(0)
  private val maxVal = Integer.MAX_VALUE

  def nextSequence() = {
    if (sequence.incrementAndGet() == maxVal) {
      val result = sequence.get()
      sequence.set(0)
      result
    } else {
      sequence.get()
    }

  }
}
