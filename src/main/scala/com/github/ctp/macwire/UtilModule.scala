package com.github.ctp.macwire

import com.github.ctp.util.{FileHelper, UuidGenerator}
import com.softwaremill.macwire.wire

trait UtilModule {
  lazy val fileHelper = wire[FileHelper]
  lazy val uuidGenerator = wire[UuidGenerator]
}
