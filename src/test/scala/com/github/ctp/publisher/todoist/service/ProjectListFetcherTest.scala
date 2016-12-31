package com.github.ctp.publisher.todoist.service

import com.github.ctp.domain.{TodoistUser, UserData}
import com.github.ctp.publisher.todoist.dto.Project
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class ProjectListFetcherTest extends FlatSpec with MockFactory with Matchers {
  "A project list fetcher" should "convert response from JSON to objects" in {
    val httpRunner = stub[HttpRunner]
    val userData = UserData("test", Some(TodoistUser(enabled = true, Some("12345678"))))
    (httpRunner.getProjects _).when(userData).returns(
      """
        |{
        |   "sync_token":"gVZmrsRopn2Ieg2vJ",
        |   "temp_id_mapping":{
        |
        |   },
        |   "full_sync":true,
        |   "projects":[
        |      {
        |         "name":"project 1",
        |         "color":9,
        |         "is_deleted":0,
        |         "collapsed":0,
        |         "id":123,
        |         "parent_id":null,
        |         "item_order":1,
        |         "indent":1,
        |         "shared":false,
        |         "is_archived":0
        |      },
        |      {
        |         "name":"project 2",
        |         "color":9,
        |         "is_deleted":0,
        |         "collapsed":0,
        |         "id":234,
        |         "parent_id":null,
        |         "item_order":1,
        |         "indent":1,
        |         "shared":false,
        |         "is_archived":0
        |      }
        |   ]
        |}
      """.stripMargin)
    val sut = new ProjectListFetcher(httpRunner)
    sut.fetchProjectsOfUser(userData) shouldBe Seq(
      Project("project 1", 123),
      Project("project 2", 234)
    )
  }

  it should "work if no projects is returned" in {
    val httpRunner = stub[HttpRunner]
    val userData = UserData("test", Some(TodoistUser(enabled = true, Some("12345678"))))
    (httpRunner.getProjects _).when(userData).returns(
      """
        |{
        |   "sync_token":"gVZmrsRopn2Ieg2vJ",
        |   "temp_id_mapping":{
        |
        |   },
        |   "full_sync":true
        |}
      """.stripMargin)
    val sut = new ProjectListFetcher(httpRunner)
    sut.fetchProjectsOfUser(userData) should have size 0
  }
}
