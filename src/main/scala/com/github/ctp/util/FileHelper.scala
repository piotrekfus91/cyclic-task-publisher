package com.github.ctp.util

import java.io.{FileNotFoundException, PrintWriter}

import scala.io.Source

class FileHelper {
  def write(str: String, filePath: String): Unit = {
    new PrintWriter(filePath) {
      write(str)
      close()
    }
  }

  def read(filePath: String): String = {
    try {
      Source.fromFile(filePath).getLines.mkString("\n")
    } catch {
      case _: FileNotFoundException => ""
    }
  }
}
