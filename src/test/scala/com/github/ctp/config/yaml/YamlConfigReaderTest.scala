package com.github.ctp.config.yaml

import com.github.ctp.domain._
import com.github.ctp.util.FileHelper
import com.softwaremill.tagging._
import org.scalatest.{FlatSpec, Matchers}

class YamlConfigReaderTest extends FlatSpec with Matchers {
  "A yaml config reader" should "load config" in {
    val yamlConfigReader = new YamlConfigReader(
        new FileHelper,
        "/config/yaml/reader-test.yml".taggedWith[YamlConfigFilePath],
        true.taggedWith[YamlConfigIsRelative])

    yamlConfigReader.read() shouldBe Config(
      Map(
        ("piotrek", UserTasks(
          List(
            Task("description one"),
            Task("description two")
          )
        ))
      ),
      Map(
        ("piotrek", UserData(
          Some(TodoistUser(enabled = true, Some("123456")))
        )),
        ("zenobiusz", UserData(
          None
        )),
        ("frydwulfa", UserData(
          Some(TodoistUser(enabled = false))
        ))
      )
    )
  }
}
