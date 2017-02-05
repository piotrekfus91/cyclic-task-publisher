package com.github.ctp.test

import java.time.LocalDateTime

import com.github.ctp.util.DateTimeProvider

trait TestDateTimeProvider {
  val dateTimeProvider = new DateTimeProvider {
    var dateTime = LocalDateTime.now()

    override def now: LocalDateTime = dateTime
    def update = dateTime = LocalDateTime.now
  }

  def nowDateTime = dateTimeProvider.now
}
