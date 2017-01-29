package com.github.ctp.config

import com.github.ctp.config.domain.Config

trait ConfigReader {
  def read(): Config
}
