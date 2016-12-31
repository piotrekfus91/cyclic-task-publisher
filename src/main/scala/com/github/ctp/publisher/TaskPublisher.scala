package com.github.ctp.publisher

import com.github.ctp.domain.{Task, UserData}

trait TaskPublisher {
  def publish(userData: UserData, task: Task)
}
