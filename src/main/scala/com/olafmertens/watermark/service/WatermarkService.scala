package com.olafmertens.watermark.service

import akka.actor.{ActorSystem, Props}
import com.olafmertens.watermark.model.{Document, ProcessingStatus, Ticket}
import com.typesafe.scalalogging.LazyLogging

object WatermarkService extends LazyLogging {

  val system = ActorSystem("watermarkSystem")

  private val watermarkActor = system.actorOf(Props[WatermarkActor], name = "watermarkActor")

  /**
    * Starts the creation of a watermark for a document.
    *
    * @param doc the document
    * @return a ticket to fetch the result
    */
  def addWatermark(doc: Document): Ticket = {
    logger.debug("->({})", doc)

    val ticket = createNewTicket(doc)

    // update processing status
    ResultRepository.setResult(ticket, ProcessingStatus.Processing, None)

    watermarkActor ! WatermarkMessage(ticket, doc)

    return ticket
  }

  /**
    * Returns the document with a watermark or [[None]] when the watermark processing has not been finished yet.
    *
    * @param ticket
    * @return the document with a watermark
    */
  def getResult(ticket: Ticket): Option[Document] = {
    logger.debug("->({})", ticket)

    ResultRepository.getResult(ticket)
  }

  /**
    * Returns the current processing status.
    *
    * @param ticket
    * @return processing status
    */
  def getStatus(ticket: Ticket): ProcessingStatus.Value = {
    logger.debug("->({})", ticket)

    ResultRepository.getStatus(ticket)
  }

  private def createNewTicket(doc: Document) = Ticket(System.currentTimeMillis() + "_" + doc.hashCode())
}
