package com.olafmertens.watermark.service

import akka.actor.{Actor, Props}
import com.olafmertens.watermark.model.{Document, ProcessingStatus, Ticket}
import com.typesafe.scalalogging.LazyLogging

/**
  * Holds the current processing status and the resulting watermarked documents.
  *
  * This repository holds all data in memory and does not persist them.
  * After a certain amount of time ([[ServiceConfig.storageTime]]) the documents are deleted automatically.
  *
  * This object is thread safe.
  *
  * TODO: This should be an interface to be able to replace this in-memory implementation with a persistent repository
  * via dependency injection.
  */
object ResultRepository extends LazyLogging {

  private case class Result(status: ProcessingStatus.Value, document: Option[Document])

  private var resultMap = Map[Ticket, Result]()

  private class CleanupActor extends Actor {
    def receive = {
      case t: Ticket => cleanup(t)
    }
  }

  private val cleanupActor = WatermarkService.system.actorOf(Props[CleanupActor], name = "cleanupActor")

  import WatermarkService.system.dispatcher

  /**
    * Returns the watermarked document.
    *
    * @param ticket
    * @return the watermarked document or [[None]], if the document processing has not been finished yet
    *         or the ticket is unknown
    */
  def getResult(ticket: Ticket): Option[Document] = {
    logger.debug("->({})", ticket)

    return resultMap.get(ticket).flatMap(r => r.document)
  }

  /**
    * Returns the processing status.
    *
    * @param ticket
    * @return the processing status
    */
  def getStatus(ticket: Ticket): ProcessingStatus.Value = {
    logger.debug("->({})", ticket)

    return resultMap.get(ticket).flatMap(r => Some(r.status)).getOrElse(ProcessingStatus.Unknown)
  }

  /**
    * Sets the processing status and the resulting document for a ticket.
    *
    * @param ticket
    * @param status
    * @param document
    */
  def setResult(ticket: Ticket, status: ProcessingStatus.Value, document: Option[Document]): Unit = {
    logger.debug("->({},{},{})", ticket, status, document)

    if (status == ProcessingStatus.Finished) {
      assert(document.isInstanceOf[Some[Document]])
    }

    resultMap = resultMap + (ticket -> Result(status, document))

    if (status == ProcessingStatus.Finished) {

      // prevent the result to be stored forever
      WatermarkService.system.scheduler.scheduleOnce(ServiceConfig.storageTime, cleanupActor, ticket)
    }
  }

  /**
    * Deletes all stored data for a given ticket.
    *
    * @param ticket
    */
  def cleanup(ticket: Ticket): Unit = {
    logger.debug("->({})", ticket)

    resultMap = resultMap - ticket
  }
}
