package com.github.ctp.util

import scala.io.Source

object FileReader {
  def read(filePath: String): String = {
    Source.fromFile(filePath).getLines.mkString("\n")
  }
}
