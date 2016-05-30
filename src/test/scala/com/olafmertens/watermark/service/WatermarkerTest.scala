package com.olafmertens.watermark.service

import com.olafmertens.watermark.model.{Book, Journal, Topic}
import org.junit.Assert._
import org.junit.{Before, Test}
import spray.json.DefaultJsonProtocol._
import spray.json._

class WatermarkerTest {

  @Before
  def setConfig() {
    ServiceConfig.setDefaults()
  }

  @Test
  def test_calculateWatermark_noTopic() {

    val watermark = Watermarker.calculateWatermark("journal", "my title", "my author")
    println(watermark)

    assertEquals(Map("content" -> "journal", "title" -> "my title", "author" -> "my author"),
      watermark.parseJson.convertTo[Map[String, String]])
  }

  @Test
  def test_calculateWatermark_quotesInString() {

    val watermark = Watermarker.calculateWatermark("journal", "my \"title'", "my author")
    println(watermark)

    assertEquals(Map("content" -> "journal", "title" -> "my \"title'", "author" -> "my author"),
      watermark.parseJson.convertTo[Map[String, String]])
  }

  @Test
  def test_calculateWatermark_withTopic() {

    val watermark = Watermarker.calculateWatermark("journal", "my title", "my author", Some(Topic.Media))
    println(watermark)

    assertEquals(Map("content" -> "journal", "title" -> "my title", "author" -> "my author", "topic" -> "Media"),
      watermark.parseJson.convertTo[Map[String, String]])
  }

  @Test
  def test_addWatermark_book() {
    val book1 = Watermarker.addWatermark(Book(Topic.Business, "my title", "my author", Option.empty))

    assertEquals(Map("content" -> "journal", "title" -> "my title", "author" -> "my author", "topic" -> "Business"),
      book1.watermark.get.parseJson.convertTo[Map[String, String]])

    // re-add watermark
    val book2 = Watermarker.addWatermark(book1)

    assertEquals(book1, book2)
  }

  @Test
  def test_addWatermark_journal() {
    val journal1 = Watermarker.addWatermark(Journal("my title", "my author", Option.empty))

    assertEquals(Map("content" -> "journal", "title" -> "my title", "author" -> "my author"),
      journal1.watermark.get.parseJson.convertTo[Map[String, String]])

    // re-add watermark
    val journal2 = Watermarker.addWatermark(journal1)

    assertEquals(journal1, journal2)
  }
}