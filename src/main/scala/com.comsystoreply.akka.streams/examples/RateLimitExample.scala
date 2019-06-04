package com.comsystoreply.akka.streams.examples

import akka.stream.ThrottleMode
import akka.stream.scaladsl.Source
import com.comsystoreply.akka.streams.examples.Service._

import scala.concurrent.duration._

object RateLimitExample extends App with Setup {


  override def main(args: Array[String]): Unit = {
    rateLimitedStream
  }

  def rateLimitedStream = Source.tick(0.millis, 10.millis, ())
    .map(_ => {
      val req = Request(nextSequence(), "hello")
      println(s"sending req $req")
      req
    })
    .throttle(elements = 20, per = 1.second, maximumBurst = 50, mode = ThrottleMode.shaping)
    .mapAsync(10)(request)
    .runForeach(
      println
    )

  def rateLimitedStreamWithoutOrder = Source.tick(0.millis, 10.millis, ())
    .map(_ => {
      val req = Request(nextSequence(), "hello")
      println(s"sending req $req")
      req
    })
    .mapAsyncUnordered(10)(request)
    .runForeach(
      println
    )
}
