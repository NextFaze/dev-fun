val buildSrcKotlinVersion: String by extra(findProperty("buildSrc.kotlin.version")?.toString() ?: embeddedKotlinVersion)

val kotlinVersion by extra("1.2.71")
val dokkaVersion by extra("0.9.17")
val agpVersion by extra("3.3.0-alpha11")
