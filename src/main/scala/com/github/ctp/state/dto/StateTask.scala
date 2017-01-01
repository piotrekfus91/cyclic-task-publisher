package com.github.ctp.state.dto

import java.time.ZonedDateTime

case class StateTask(user: String, description: String, last: Map[String, ZonedDateTime])
