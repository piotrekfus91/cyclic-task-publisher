package com.github.ctp.util

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source

class FileHelperTest extends FlatSpec with Matchers with BeforeAndAfterAll {
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

  it should "write file content" in {
    val sut = new FileHelper
    sut.write("test file", "/tmp/ctptestfile")

    Source.fromFile("/tmp/ctptestfile").getLines.mkString("") shouldBe "test file"
  }
}
