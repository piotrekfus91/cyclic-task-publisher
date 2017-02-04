package com.github.ctp.util

import java.time.LocalDateTime

trait DateTimeProvider {
  def now: LocalDateTime
}

class DateTimeProviderImpl extends DateTimeProvider {
  override def now: LocalDateTime = LocalDateTime.now
}