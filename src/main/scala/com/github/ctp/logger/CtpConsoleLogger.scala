package com.github.ctp.logger

class CtpConsoleLogger extends CtpLogger {
  override def log(msg: String): Unit = print(msg)
}
