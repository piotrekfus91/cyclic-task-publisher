package com.github.ctp.publisher.todoist.service

import com.github.ctp.domain.UserData
import com.github.ctp.publisher.todoist.dto.Project
import com.typesafe.scalalogging.LazyLogging

class ProjectListManagerImpl(private val projectListFetcher: ProjectListFetcher) extends LazyLogging with ProjectListManager {
  private var userProjects = Map[String, List[Project]]()

  def refreshUserProjects(userData: UserData) = {
    userProjects = userProjects + (userData.name -> projectListFetcher.fetchProjectsOfUser(userData))
  }

  def getUserProjectByName(userData: UserData, projectName: String): Option[Project] = {
    val maybeMaybeProject = for {
      userProjects <- userProjects.get(userData.name)
      project = userProjects.find(_.name == projectName)
    } yield project
    logger.debug(s"project $projectName for ${userData.name} is $maybeMaybeProject")
    maybeMaybeProject.getOrElse(None)
  }
}
