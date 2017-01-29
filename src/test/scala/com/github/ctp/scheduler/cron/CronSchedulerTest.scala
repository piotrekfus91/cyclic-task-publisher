package com.github.ctp.scheduler.cron

import java.time.LocalDateTime

import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, PropSpec}

class CronSchedulerTest extends PropSpec with PropertyChecks with Matchers {
  val parser = new CronScheduleParser

  property("parse simple string") {
    val sut = parser.parse("* * * * *").right.get

    val timeNow = LocalDateTime.of(2017, 1, 29, 14, 6, 47, 0)
    val expectedNextExecution = LocalDateTime.of(2017, 1, 29, 14, 7, 0, 0)

    sut.getNextTime(timeNow) shouldBe expectedNextExecution
  }

  val specifics = Table(
    ("formula", "now", "next"),
    ("*/5 13 * * *", LocalDateTime.of(2017, 1, 29, 11, 13), LocalDateTime.of(2017, 1, 29, 13, 0)),
    ("*/5 * * * *", LocalDateTime.of(2017, 1, 29, 11, 13), LocalDateTime.of(2017, 1, 29, 11, 15)),
    ("1,5-15 * * * *", LocalDateTime.of(2017, 1, 29, 11, 13), LocalDateTime.of(2017, 1, 29, 11, 14)),
    ("1,5-15 * * * *", LocalDateTime.of(2017, 1, 29, 11, 16), LocalDateTime.of(2017, 1, 29, 12, 1)),
    ("* 0 1 * *", LocalDateTime.of(2017, 1, 29, 11, 16), LocalDateTime.of(2017, 2, 1, 0, 0))
  )

  property("parse string in specific way") {
    forAll(specifics) { (formula, now, next) =>
      val sut = parser.parse(formula).right.get
      sut.getNextTime(now) shouldBe next
    }
  }
}
