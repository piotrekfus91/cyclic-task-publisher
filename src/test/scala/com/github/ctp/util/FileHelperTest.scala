package com.github.ctp.util

import org.scalatest.{FlatSpec, Matchers}

class FileHelperTest extends FlatSpec with Matchers {
  "A file reader" should "read file" in {
    val sut = new FileHelper
    val filePath = getClass.getResource("/util/reader-test.txt").getPath
    sut.read(filePath) shouldBe """|test file
                                   |    tabs
                                   |ends here""".stripMargin
  }

  it should "return empty string if file not exists" in {
    val sut = new FileHelper
    val filePath = "not existing"
    sut.read(filePath) shouldBe ""
  }
}
