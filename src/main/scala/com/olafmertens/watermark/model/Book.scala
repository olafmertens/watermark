package com.olafmertens.watermark.model

case class Book(val topic: Topic.Value,
                override val title: String,
                override val author: String,
                override val watermark: Option[String])
  extends Document(title,author, watermark)