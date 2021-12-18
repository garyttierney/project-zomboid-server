import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.JavaVersion.VERSION_11

plugins {
    java
    kotlin("jvm") version "1.5.31"
    id("com.github.johnrengelman.shadow") version ("7.1.0")
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
}

val pzDirectory: String by rootProject

dependencies {
    compileOnly(files(pzDirectory))
    implementation("net.bytebuddy:byte-buddy:1.12.3")
    implementation("net.bytebuddy:byte-buddy-agent:1.12.3")
    implementation("io.javalin:javalin:4.1.1")
}


tasks.getByName<Jar>("jar") {
    archiveFileName.set("pz-agent-lib.jar")
}

tasks.getByName<ShadowJar>("shadowJar") {
    manifest {
        attributes["Premain-Class"] = "com.github.garyttierney.zomboid.server.ZomboidServerPatcher"
        attributes["Can-Retransform-Classes"] = true
        attributes["Can-Redefine-Classes"] = true
    }

    archiveFileName.set("pz-agent.jar")
    relocate("net.bytebuddy", "com.github.garyttierney.zomboid.bundled.bytebuddy")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}