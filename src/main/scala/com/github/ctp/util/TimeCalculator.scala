package com.github.ctp.util

import java.time.{LocalDateTime, ZoneId}

import scala.concurrent.duration._

class TimeCalculator {
  def calculateDuration(from: LocalDateTime, to: LocalDateTime): FiniteDuration = {
    val fromMillis = from.atZone(ZoneId.systemDefault()).toInstant.toEpochMilli
    val toMillis = to.atZone(ZoneId.systemDefault()).toInstant.toEpochMilli
    (toMillis - fromMillis).millis
  }
}
