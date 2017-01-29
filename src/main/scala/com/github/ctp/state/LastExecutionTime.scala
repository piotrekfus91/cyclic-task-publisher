package com.github.ctp.state

import java.time.ZonedDateTime

case class LastExecutionTime(user: String, description: String, last: Map[String, ZonedDateTime])