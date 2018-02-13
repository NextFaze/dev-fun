@file:Suppress("unused")

package com.nextfaze.devfun

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.`kapt`(dependencyNotation: Any): Dependency = add("kapt", dependencyNotation)
