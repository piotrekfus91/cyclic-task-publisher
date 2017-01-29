package com.github.ctp.config.yaml

import com.github.ctp.config.domain._
import com.github.ctp.util.FileHelper
import org.scalatest.{FlatSpec, Matchers}

class YamlConfigReaderTest extends FlatSpec with Matchers {
  "A yaml config reader" should "load config" in {
    val yamlConfigReader = new YamlConfigReader(
        new FileHelper,
        "/config/yaml/reader-test.yml",
        true
    )

    yamlConfigReader.read() shouldBe Config(
      Map(
        ("piotrek", UserTasks(
          List(
            Task("description one", "test project"),
            Task("description two", "test project", Map("cron" -> "*/5 1-3 * * *"))
          )
        ))
      ),
      Map(
        ("piotrek", UserData(
          "piotrek", Some(TodoistUser(enabled = true, Some("123456")))
        )),
        ("zenobiusz", UserData(
          "zenobiusz", None
        )),
        ("frydwulfa", UserData(
          "frydwulfa", Some(TodoistUser(enabled = false))
        ))
      )
    )
  }
}
