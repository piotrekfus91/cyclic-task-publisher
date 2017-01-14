package com.github.ctp.guice

import com.github.ctp.config.ConfigReader
import com.github.ctp.config.yaml.YamlConfigReader
import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule

class ConfigModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[String].annotatedWithName("yamlConfigFilePath").toInstance("config.yml")
    bind[Boolean].annotatedWithName("yamlConfigIsRelative").toInstance(true)
    bind[ConfigReader].to[YamlConfigReader]
  }
}
