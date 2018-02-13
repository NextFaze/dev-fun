package com.nextfaze.devfun

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.task

inline fun <reified T : Task> Project.getOrCreateTask(taskName: String, body: T.() -> Unit): T =
    (tasks.findByName(taskName)?.let { it as T } ?: task<T>(taskName)).apply { body() }
