plugins {
    kotlin("jvm") version("1.6.10")
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    google()
    maven("https://repo.openrs2.org/repository/openrs2/")
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())// https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")

    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.openrs2:fernflower:1.1.1")
}