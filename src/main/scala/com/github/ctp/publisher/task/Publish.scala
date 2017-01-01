package com.github.ctp.publisher.task

import com.github.ctp.domain.{Task, UserData}

case class Publish(userData: UserData, task: Task)
