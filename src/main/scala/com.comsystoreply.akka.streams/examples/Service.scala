package com.comsystoreply.akka.streams.examples

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object Service {

  case class Request(id: Int, payload: String)

  case class Response(requestId: Int, response: String)



  def request(request: Request)(implicit ec: ExecutionContext) = {
    Future{
      Thread.sleep(dynamicDelay)
      Response(request.id, "got it")
    }
  }

  val random: Random = Random
  private val start = 500
  private val end = 1000
  def dynamicDelay: Long = start + random.nextInt( (end - start) + 1 )
}


