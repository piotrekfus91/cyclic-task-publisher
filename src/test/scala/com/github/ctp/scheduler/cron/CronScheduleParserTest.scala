package com.github.ctp.scheduler.cron

import org.scalatest.{FlatSpec, Matchers}

class CronScheduleParserTest extends FlatSpec with Matchers {
  val sut = new CronScheduleParser

  "CronScheduleParser" should "validate input" in {
    sut.parse("bla bla").left.get should include ("Cron expression")
  }

  it should "create valid scheduler" in {
    sut.parse("* * * * *").isRight shouldBe true
  }
}
