package com.github.ctp.scheduler.cron

import java.time.{LocalDateTime, ZoneId}

import com.cronutils.model.Cron
import com.cronutils.model.time.ExecutionTime
import com.github.ctp.scheduler.Scheduler

class CronScheduler(cron: Cron) extends Scheduler {
  override def getNextTime(dateTimeFrom: LocalDateTime): LocalDateTime = {
    val executionTime = ExecutionTime.forCron(cron)
    val nextExecutionTime = executionTime.nextExecution(dateTimeFrom.atZone(ZoneId.systemDefault()))
    nextExecutionTime.toLocalDateTime
  }
}
