package com.github.ctp.state.dto

import java.time.LocalDateTime

case class StateTask(user: String, description: String, var last: LocalDateTime)
