package com.github.ctp.config.yaml

import com.github.ctp.domain.{AllTasks, Config, Task, UserTasks}
import org.scalatest.{FlatSpec, Matchers}

class YamlConfigReaderTest extends FlatSpec with Matchers {
  "A yaml config reader" should "load config" in {
    val yamlConfigReader = new YamlConfigReader("/config/yaml/reader-test.yml", isRelative = true)
    yamlConfigReader.read() shouldBe Config(
      AllTasks(
        Map(
          ("piotrek", UserTasks(
            List(
              Task("description one"),
              Task("description two")
            )
          ))
        )
      )
    )
  }
}
