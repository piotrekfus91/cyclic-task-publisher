package com.github.ctp.domain

case class Config(allTasks: Map[String, UserTasks], users: Map[String, UserData])