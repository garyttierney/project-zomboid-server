package com.github.garyttierney.zomboid

data class ProjectZomboidSettings(
    val mainClass: String,
    val classpath: List<String>,
    val vmArgs: List<String>
)
