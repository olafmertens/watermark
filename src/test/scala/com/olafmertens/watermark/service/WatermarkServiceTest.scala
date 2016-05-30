package com.olafmertens.watermark.service

import com.olafmertens.watermark.model._
import com.typesafe.scalalogging.LazyLogging
import org.junit.{Before, Test}
import org.junit.Assert._

import scala.concurrent.duration._
import scala.language.postfixOps

class WatermarkServiceTest extends LazyLogging {

  @Before
  def setConfig() {
    ServiceConfig.setDefaults()
  }

  @Test
  def test_addWatermark_book() {

    ServiceConfig.processingDelay = 1 second

    val book = Book(Topic.Business, "Blafasel", "Olaf", Option.empty)

    val ticket = WatermarkService.addWatermark(book)
    logger.info("got ticket: " + ticket)

    Thread.sleep(500)

    assertEquals(None, WatermarkService.getResult(ticket))
    assertEquals(ProcessingStatus.Processing, WatermarkService.getStatus(ticket))

    Thread.sleep(1000)

    assertEquals(ProcessingStatus.Finished, WatermarkService.getStatus(ticket))
    val watermark = WatermarkService.getResult(ticket).get.watermark.get
    logger.info("watermark: {}", watermark)

    assertTrue(watermark.contains("\"author\":\"Olaf\""))
  }

  @Test
  def test_addWatermark_journal() {

    ServiceConfig.processingDelay = 1 second

    val journal = Journal("Blafasel", "Olaf", Option.empty)

    val ticket = WatermarkService.addWatermark(journal)
    logger.info("got ticket: " + ticket)

    Thread.sleep(500)

    assertEquals(None, WatermarkService.getResult(ticket))
    assertEquals(ProcessingStatus.Processing, WatermarkService.getStatus(ticket))

    Thread.sleep(1000)

    assertEquals(ProcessingStatus.Finished, WatermarkService.getStatus(ticket))
    val watermark = WatermarkService.getResult(ticket).get.watermark.get
    logger.info("watermark: {}", watermark)

    assertTrue(watermark.contains("\"author\":\"Olaf\""))
  }

  @Test
  def test_addWatermark_shortStorageTime() {

    ServiceConfig.processingDelay = 1 second

    ServiceConfig.storageTime = 1 second

    val book = Book(Topic.Business, "Blafasel", "Olaf", Option.empty)

    val ticket = WatermarkService.addWatermark(book)
    logger.info("got ticket: " + ticket)

    Thread.sleep(500)

    assertEquals(None, WatermarkService.getResult(ticket))
    assertEquals(ProcessingStatus.Processing, WatermarkService.getStatus(ticket))

    Thread.sleep(2000)

    assertEquals(ProcessingStatus.Unknown, WatermarkService.getStatus(ticket))
  }

  @Test
  def test_invalidTicket() {

    val ticket = Ticket("xxx")
    assertEquals(ProcessingStatus.Unknown, WatermarkService.getStatus(ticket))
    assertEquals(None, WatermarkService.getResult(ticket))
  }
}
