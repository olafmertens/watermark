package com.olafmertens.watermark.service

import spray.json._
import DefaultJsonProtocol._
import com.olafmertens.watermark.model.{Book, Document, Journal, Topic}
import com.typesafe.scalalogging.LazyLogging

object Watermarker extends LazyLogging {

  /**
    * Adds a watermark to a document.
    * @param document
    * @return the watermarked document
    */
  def addWatermark(document: Document): Document = {
    logger.debug("->({})", document)

    document match {
      case Book(topic, title, author, watermark) =>
        Book(topic, title, author, Some(calculateWatermark("journal", title, author, Some(topic))))

      case Journal(title, author, watermark) =>
        Journal(title, author, Some(calculateWatermark("journal", title, author)))
    }
  }

  /**
    * Creates a watermark string in JSON style.
    *
    * @param content
    * @param title
    * @param author
    * @param topic
    * @return the watermark string
    */
  def calculateWatermark(content: String, title: String, author: String, topic: Option[Topic.Value] = None): String = {
    logger.debug("->({},{},{},{})", content, title, author, topic)

    Thread.sleep(ServiceConfig.processingDelay.toMillis)

    val map = Map(
      "content" -> content,
      "title" -> title,
      "author" -> author
    )

    topic match {
      case Some(t) => (map + ("topic" -> t.toString())).toJson.toString
      case None => map.toJson.toString
    }
  }
}
