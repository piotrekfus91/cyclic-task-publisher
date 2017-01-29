package com.github.ctp.config.domain

case class Config(allTasks: Map[String, UserTasks], users: Map[String, UserData])