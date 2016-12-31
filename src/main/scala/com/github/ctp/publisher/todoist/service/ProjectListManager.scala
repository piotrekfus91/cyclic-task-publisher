package com.github.ctp.publisher.todoist.service
import com.github.ctp.domain.UserData
import com.github.ctp.publisher.todoist.dto.Project

trait ProjectListManager {
  def refreshUserProjects(userData: UserData)
  def getUserProjectByName(userData: UserData, projectName: String): Option[Project]
}
