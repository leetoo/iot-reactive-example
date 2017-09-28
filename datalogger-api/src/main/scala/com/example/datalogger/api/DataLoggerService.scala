package com.example.datalogger.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api._
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.transport.Method

/**
  * This trait defines all common api endpoints required for a data logger service.
  *
  * Initially, we may consider that there is only one endpoint for include new measures.
  *
  * @author jazumaquero
  */
trait DataLoggerService extends Service {
  /**
    * Endpoint used to push new measures from some device.
    *
    * @return
    */
  def addMeasure(): ServiceCall[AddMeasure, Done]

  /**
    * Allows publishing the measure to some topic
    *
    * @return
    */
  def publishMeasure(): Topic[AddMeasure]

  override def descriptor: Descriptor = {
    import Service._
    named("logger").withCalls(
      restCall(Method.POST, "/measure", addMeasure _)
    ).withTopics(
      topic[AddMeasure](DataLoggerService.AddMeasureTopic, publishMeasure)
    ).withAutoAcl(true)
  }
}

/**
  * Companion class from {@link DataLoggerService} within all static methods
  */
object DataLoggerService {
  val AddMeasureTopic: String = "pub_measure"
}