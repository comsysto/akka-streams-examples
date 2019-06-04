package com.comsystoreply.akka.streams.examples

import akka.stream.scaladsl.{Sink, Source}
import spray.json._

import scala.util.{Failure, Success, Try}

object DecomposingExample extends App with Setup with DefaultJsonProtocol {

  case class Data(id: String, timestamp: Long, measurements: Map[String, Double])
  case class Measurement(id: String, timestamp: Long, signal: String, value: Double)

  object DataJsonProtocol extends DefaultJsonProtocol {
    implicit val dataFormat: RootJsonFormat[Data] = jsonFormat3(Data)
  }

  override def main(args: Array[String]): Unit = {

    val json =
      """
        |{
        |	"id": "c75cb448-df0e-4692-8e06-0321b7703992",
        |	"timestamp": 1486925114,
        |	"measurements": {
        |		"power": 1.7,
        |		"rotor_speed": 3.9,
        |		"wind_speed": 10.1
        |	}
        |}
      """.stripMargin

    Source.single(json)
      .mapConcat[Measurement]( s => toImmutable[Measurement](parse.apply(s)))
      .runWith(Sink.foreach(println))
  }

  def parse: String => Iterable[Measurement] = json => {
    import DataJsonProtocol._

    Try(json.parseJson.convertTo[Data]) match {
      case Success(data) => data
        .measurements
        .map(
          keyVal => Measurement(data.id, data.timestamp, keyVal._1, keyVal._2))
      case Failure(ex) =>
        println(s"error while parsing json $ex")
        ex.printStackTrace()
        Seq.empty
    }
  }

  def toImmutable[A](elements: Iterable[A]) =
    new scala.collection.immutable.Iterable[A] {
      override def iterator: Iterator[A] = elements.toIterator
    }

}



