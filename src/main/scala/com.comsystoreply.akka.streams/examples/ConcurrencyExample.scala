package com.comsystoreply.akka.streams.examples

import java.time.{Clock, Instant}

import akka.Done
import akka.stream.scaladsl.{Sink, Source}

import scala.concurrent.Future
import scala.io.{Source => IOSource}
import scala.util.{Failure, Success}

object ConcurrencyExample extends App with Setup {

  override def main(args: Array[String]): Unit = {

    val text = IOSource.fromResource("large_1.txt").mkString
    val textCopy = new String(text)
    val clock = Clock.systemUTC()
    val maxRange = 100

    singleThreadStream(clock)
    concurrentStream(clock)

    def measureTime: (String, Instant, Future[Done]) => Unit = {
      (name, start, done) =>
        done.onComplete {
          case Success(_) =>
            println(s"stream $name completed successfully. Duration:  ${Instant.now().toEpochMilli - start.toEpochMilli} ms")
          case Failure(ex) =>
            println(s"stream $name completed with failure : $ex")
        }
    }

    def singleThreadStream(clock: Clock) = {
      val start = clock.instant()
      Source(1 to maxRange)
        .watchTermination()((_, f) => measureTime("first", start, f))
        .map(_ => Gzip.compress(text.getBytes))
        .map(gzipped => Gzip.decompress(gzipped))
        .runWith(Sink.ignore)
    }



    def concurrentStream(clock: Clock) = {
      val start = clock.instant()
      Source(1 to maxRange)
        .watchTermination()((_, f) => measureTime("second", start, f))
        .map(_ => Gzip.compress(textCopy.getBytes))
        .async
        .map(gzipped => Gzip.decompress(gzipped))
        .runWith(Sink.ignore)
    }
  }
}
