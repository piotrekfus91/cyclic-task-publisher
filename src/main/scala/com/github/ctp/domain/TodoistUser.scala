package com.github.ctp.domain

case class TodoistUser(enabled: Boolean, apiToken: Option[String] = None)