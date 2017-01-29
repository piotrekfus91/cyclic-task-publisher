package com.github.ctp.state

import java.time.LocalDateTime

case class LastExecutionTime(user: String, description: String, last: Map[String, LocalDateTime])