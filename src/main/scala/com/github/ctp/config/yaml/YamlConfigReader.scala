package com.github.ctp.config.yaml

import com.github.ctp.config.ConfigReader
import com.github.ctp.config.yaml.MoultingYamlFormats._
import com.github.ctp.domain.Config
import com.github.ctp.util.FileHelper
import com.softwaremill.tagging._
import net.jcazevedo.moultingyaml._

class YamlConfigReader(
                        private val fileReader: FileHelper,
                        private val filePath: String @@ YamlConfigFilePath,
                        private val isRelative: Boolean @@ YamlConfigIsRelative) extends ConfigReader {

  override def read(): Config = {
    val readConfig = fileReader.read(buildPath).parseYaml
    readConfig.convertTo[Config]
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