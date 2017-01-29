package com.github.ctp.scheduler

import java.time.LocalDateTime

trait Scheduler {
  def getNextTime(localDateTime: LocalDateTime): LocalDateTime
}
