package com.github.ctp.macwire

import com.github.ctp.config.yaml.{YamlConfigFilePath, YamlConfigIsRelative, YamlConfigReader}
import com.github.ctp.util.FileHelper
import com.softwaremill.macwire._
import com.softwaremill.tagging._

trait ConfigModule {
  lazy val yamlConfigFilePath = "config.yml".taggedWith[YamlConfigFilePath]
  lazy val isRelative = true.taggedWith[YamlConfigIsRelative]

  lazy val fileHelper = wire[FileHelper]
  lazy val configReader: YamlConfigReader = wire[YamlConfigReader]
}
