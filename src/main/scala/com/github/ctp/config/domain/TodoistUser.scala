package com.github.ctp.config.domain

case class TodoistUser(enabled: Boolean, apiToken: Option[String] = None)