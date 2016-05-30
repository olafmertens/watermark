package com.olafmertens.watermark.model

case class Journal(override val title: String,
                   override val author: String,
                   override val watermark: Option[String])
  extends Document(title, author, watermark)
