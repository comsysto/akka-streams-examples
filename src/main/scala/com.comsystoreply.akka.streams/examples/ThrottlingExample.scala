package com.comsystoreply.akka.streams.examples

import akka.stream.ThrottleMode
import akka.stream.scaladsl.{Sink, Source}
import scala.concurrent.duration._

object ThrottlingExample extends App with Setup {

  override def main(args: Array[String]): Unit = {

    Source.tick(0.millis, 10.millis, ())
      .map(_ => Sample(nextSequence(), System.currentTimeMillis(), random.nextFloat()))
      .throttle(elements = 20, per = 1.second, maximumBurst = 50, mode = ThrottleMode.shaping)
      .runWith(Sink.foreach(println))
  }

}
