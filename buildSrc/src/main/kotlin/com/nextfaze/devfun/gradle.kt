@file:Suppress("unused")

package com.nextfaze.devfun

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/** Named as `kapt_` so it doesn't conflict when proper one added. */
fun DependencyHandler.`kapt_`(dependencyNotation: Any): Dependency? = add("kapt", dependencyNotation)
