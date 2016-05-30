package com.olafmertens.watermark.service

import com.olafmertens.watermark.model.{Document, Ticket}

/**
  * Message to send tasks to the [[WatermarkActor]].
  *
  * @param ticket processing ticket
  * @param document document to be watermarked
  */
case class WatermarkMessage(ticket:Ticket, document: Document)
