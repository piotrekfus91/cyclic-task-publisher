package com.github.ctp.util

import org.scalatest.{FlatSpec, Matchers}

class FileReaderTest extends FlatSpec with Matchers {
  "A file reader" should "read file" in {
    val filePath = getClass.getResource("/util/reader-test.txt").getPath
    FileReader.read(filePath) shouldBe """|test file
        |    tabs
        |ends here""".stripMargin
  }
}
