package com.olafmertens.watermark.service

import akka.actor.Actor
import com.olafmertens.watermark.model.{Book, Journal, ProcessingStatus}
import com.typesafe.scalalogging.LazyLogging

/**
  * Actor, that receives a document with a corresponding ticket.
  * It will add a watermark to the document and stores the document in the [[ResultRepository]].
  */
class WatermarkActor extends Actor with LazyLogging {

  def receive = {
    case WatermarkMessage(ticket, doc) => {
      logger.debug("-> WatermarkMessage({},{})", ticket, doc)

      // create watermark and store result
      doc match {
        case b:Book =>
          ResultRepository.setResult(ticket, ProcessingStatus.Finished, Some(Watermarker.addWatermark(b)))
        case j:Journal =>
          ResultRepository.setResult(ticket, ProcessingStatus.Finished, Some(Watermarker.addWatermark(j)))
      }
    }
    case _ => logger.error("invalid message")
  }
}