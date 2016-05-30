package com.olafmertens.watermark.model

import org.junit.Test
import org.junit.Assert._

class ModelTest {

  @Test
  def test_book(): Unit = {
    val book1 = new Book(Topic.Business, "Harry Potter", "Rowling", Option.empty)
    val book2 = new Book(Topic.Business, "Harry Potter", "Rowling", Option.empty)

    assertEquals(book1, book2)
  }

  @Test
  def test_book1(): Unit = {
    val book1 = new Book(Topic.Business, "Harry Potter", "Rowling", Option.empty)
    val book2 = new Book(Topic.Media, "Harry Potter", "Rowling", Option.empty)

    assertNotEquals(book1, book2)
  }

  @Test
  def test_journal(): Unit = {
    val journal1 = new Journal("Harry Potter", "Rowling", Option.empty)
    val journal2 = new Journal("Harry Potter", "Rowling", Option.empty)

    assertEquals(journal1, journal2)
  }
}
