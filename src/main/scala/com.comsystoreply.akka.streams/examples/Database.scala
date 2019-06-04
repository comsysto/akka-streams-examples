package com.comsystoreply.akka.streams.examples

import scala.collection.{immutable, mutable}
import scala.concurrent.Future

class Database {

  var storage: mutable.Map[Int, Sample] = new mutable.HashMap()

  def bulkInsert: immutable.Seq[Sample] => Unit = items => {
    items.foreach( i => storage.put(i.key, i)
    )
  }

  def bulkInsertAsync: immutable.Seq[Sample] => Future[Int] = items => {
    val count: Int = items.size
    items.foreach( i => storage.put(i.key, i)
    )
    Future.successful(count)
  }
}
