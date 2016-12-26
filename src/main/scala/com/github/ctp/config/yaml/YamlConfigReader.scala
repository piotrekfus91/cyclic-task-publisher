package com.github.ctp.config.yaml

import com.github.ctp.config.ConfigReader
import com.github.ctp.domain.Config
import com.github.ctp.util.FileReader
import net.jcazevedo.moultingyaml._
import com.github.ctp.config.yaml.MoultingYamlFormats._

class YamlConfigReader(val filePath: String = "config.yml", val isRelative: Boolean = true) extends ConfigReader {
  override def read(): Config = {
    val readConfig = FileReader.read(buildPath).parseYaml
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
