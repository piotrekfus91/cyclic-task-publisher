package com.github.ctp.publisher.todoist.dto

case class Command(commandType: String, tempId: String, uuid: String, todoistTask: TodoistTask)
