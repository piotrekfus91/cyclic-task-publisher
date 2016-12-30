package com.github.ctp.config.yaml

import com.github.ctp.config.ConfigReader
import com.github.ctp.config.yaml.MoultingYamlFormats._
import com.github.ctp.domain.{Config, UserData}
import com.github.ctp.util.FileHelper
import com.softwaremill.tagging._
import com.typesafe.scalalogging.LazyLogging
import net.jcazevedo.moultingyaml._

class YamlConfigReader(
                        private val fileReader: FileHelper,
                        private val filePath: String @@ YamlConfigFilePath,
                        private val isRelative: Boolean @@ YamlConfigIsRelative)
    extends ConfigReader with LazyLogging {

  override def read(): Config = {
    val path = buildPath
    logger.info(s"reading config $path")
    val readConfig = fileReader.read(path).parseYaml
    val config = readConfig.convertTo[Config]
    config.copy(users = fillUserData(config.users))
  }

  private def fillUserData(users: Map[String, UserData]) = {
    users.map {case (name: String, userData: UserData) => (name, userData.copy(name = name))}
  }

  private def buildPath: String = {
    if (isRelative) {
      getClass.getResource(filePath).getPath
    } else {
      filePath
    }
  }
}

trait YamlConfigFilePath
trait YamlConfigIsRelative