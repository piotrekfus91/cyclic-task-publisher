package com.github.ctp.util

import scala.io.Source

class FileHelper {
  def read(filePath: String): String = {
    Source.fromFile(filePath).getLines.mkString("\n")
  }
}
