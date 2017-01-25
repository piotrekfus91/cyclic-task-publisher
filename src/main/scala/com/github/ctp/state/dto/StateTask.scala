package com.github.ctp.state.dto

import java.time.ZonedDateTime

case class StateTask(user: String, description: String, var last: Map[String, ZonedDateTime])
