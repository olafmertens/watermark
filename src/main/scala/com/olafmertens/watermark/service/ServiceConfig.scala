package com.olafmertens.watermark.service

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Configuration values that can be overridden in unit tests.
  */
object ServiceConfig {

  private val DEFAULT_PROCESSING_DELAY = 0 seconds
  private val DEFAULT_STORAGE_TIME = 1 hour

  /**
    * processing delay to simulate real watermark processing
    */
  var processingDelay: FiniteDuration = DEFAULT_PROCESSING_DELAY

  /**
    * storage time for the watermarked document
    */
  var storageTime: FiniteDuration = DEFAULT_STORAGE_TIME

  def setDefaults(): Unit = {
    processingDelay = DEFAULT_PROCESSING_DELAY
    storageTime = DEFAULT_STORAGE_TIME
  }
}
