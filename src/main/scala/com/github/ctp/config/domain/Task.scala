package com.github.ctp.config.domain

case class Task(description: String, project: String, scheduler: Map[String, String] = Map())
