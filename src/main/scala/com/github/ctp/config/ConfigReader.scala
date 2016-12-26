package com.github.ctp.config

import com.github.ctp.domain.Config

trait ConfigReader {
  def read(): Config
}
