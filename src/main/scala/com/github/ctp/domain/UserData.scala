package com.github.ctp.domain

case class UserData(name: String, todoist: Option[TodoistUser] = None)
