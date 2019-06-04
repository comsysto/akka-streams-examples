package com.comsystoreply.akka.streams

package object examples {

  case class Sample(key: Int, timestamp: Long, value: Float)
  case class Status(value: String)
}
