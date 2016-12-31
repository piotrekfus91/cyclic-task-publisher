package com.github.ctp.macwire

import com.github.ctp.config.yaml.{YamlConfigFilePath, YamlConfigIsRelative, YamlConfigReader}
import com.softwaremill.macwire._
import com.softwaremill.tagging._

trait ConfigModule extends UtilModule {
  lazy val yamlConfigFilePath = "config.yml".taggedWith[YamlConfigFilePath]
  lazy val isRelative = true.taggedWith[YamlConfigIsRelative]

  lazy val configReader: YamlConfigReader = wire[YamlConfigReader]
}
