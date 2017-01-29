package com.github.ctp.config.domain

case class UserData(name: String, todoist: Option[TodoistUser] = None)
