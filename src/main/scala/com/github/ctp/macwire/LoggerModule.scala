package com.github.ctp.macwire

import com.github.ctp.logger.CtpConsoleLogger
import com.softwaremill.macwire._

trait LoggerModule {
  val ctpLogger = wire[CtpConsoleLogger]
}
