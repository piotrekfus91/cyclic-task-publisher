package com.github.ctp.config.domain

case class Task(description: String, project: String, schedule: Map[String, String] = Map(),
                publishers: List[String] = List())
