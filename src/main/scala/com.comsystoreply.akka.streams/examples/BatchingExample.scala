package com.comsystoreply.akka.streams.examples

import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration._

object BatchingExample extends App with Setup {


  lazy val database = new Database()

  override def main(args: Array[String]): Unit = {
    rateLimitedStreamWithoutOrder
  }

  def groupedStream = Source.tick(0.millis, 10.millis, ())
    .map(_ => Sample(randomKey(), System.currentTimeMillis(), random.nextFloat()))
    .grouped(1000)
    .map( items => {
      println(s"size of items is ${items.size}")
      database.bulkInsert(items)
    }
    )
    .runWith(Sink.ignore)

  def groupedWithinStream = Source.tick(0.millis, 10.millis, ())
    .map(_ => Sample(randomKey(), System.currentTimeMillis(), random.nextFloat()))
    .groupedWithin(1000, 1.seconds)
    .map( items => {
      println(s"size of items is ${items.size}")
      database.bulkInsert(items)
    }
    )
    .runWith(Sink.ignore)

  def rateLimitedStream = Source.tick(0.millis, 10.millis, ())
    .map(_ => Sample(randomKey(), System.currentTimeMillis(), random.nextFloat()))
    .groupedWithin(1000, 100.millis)
    .mapAsync(10)( items => {
      println(s"size of items is ${items.size}")
      database.bulkInsertAsync(items)
    }
    )
    .runWith(Sink.ignore)

  def rateLimitedStreamWithoutOrder = Source.tick(0.millis, 10.millis, ())
    .map(_ => Sample(randomKey(), System.currentTimeMillis(), random.nextFloat()))
    .groupedWithin(1000, 100.millis)
    .mapAsyncUnordered(10)( items => {
      println(s"size of items is ${items.size}")
      database.bulkInsertAsync(items)
    }
    )
    .runWith(Sink.ignore)
}
