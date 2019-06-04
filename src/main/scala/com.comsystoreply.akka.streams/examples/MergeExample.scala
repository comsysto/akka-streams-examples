package com.comsystoreply.akka.streams.examples

import akka.stream.scaladsl.{Sink, Source}

object MergeExample extends App with Setup {

  object SampleOrdering extends Ordering[Sample] {
    override def compare(x: Sample, y: Sample): Int = {
      val result = x.key.compare(y.key)
      if(result == 0) {
        x.timestamp.compareTo(y.timestamp)
      } else {
        result
      }
    }

  }

  override def main(args: Array[String]): Unit = {

    val firstSource = Source(
      List(
        Sample(1, System.currentTimeMillis(), random.nextFloat()),
        Sample(2, System.currentTimeMillis(), random.nextFloat()),
        Sample(3, System.currentTimeMillis(), random.nextFloat()),
        Sample(5, System.currentTimeMillis(), random.nextFloat()),
      )
    )

    val secondSource = Source(
      List(
        Sample(2, System.currentTimeMillis(), random.nextFloat()),
        Sample(4, System.currentTimeMillis(), random.nextFloat()),
        Sample(6, System.currentTimeMillis(), random.nextFloat()),
        Sample(7, System.currentTimeMillis(), random.nextFloat()),
      )
    )

    implicit val ordering: SampleOrdering.type = SampleOrdering
    firstSource
      .mergeSorted(secondSource)
      .runWith(Sink.foreach(
        println
      ))
  }
}
