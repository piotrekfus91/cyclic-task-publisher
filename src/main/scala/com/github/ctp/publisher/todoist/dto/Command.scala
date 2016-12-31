package com.github.ctp.publisher.todoist.dto

case class Command[A](commandType: String, temp_id: String, uuid: String, args: A)
