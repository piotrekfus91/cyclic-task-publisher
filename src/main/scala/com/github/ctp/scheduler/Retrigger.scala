package com.github.ctp.scheduler

import java.time.LocalDateTime

case class Retrigger(userName: String, description: String, system: String, lastExecutionTime: LocalDateTime)
