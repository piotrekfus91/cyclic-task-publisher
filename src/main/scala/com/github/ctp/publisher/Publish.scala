package com.github.ctp.publisher

import com.github.ctp.domain.{Task, UserData}

case class Publish(userData: UserData, task: Task)
