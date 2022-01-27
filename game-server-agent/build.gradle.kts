import com.github.garyttierney.zomboid.tasks.ProjectZomboidLaunchTask
import com.github.garyttierney.zomboid.tasks.ProjectZomboidLaunchType.*
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.JavaVersion.VERSION_11
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version ("7.1.0")
    `project-zomboid`
}

val pzDirectory: String by rootProject

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
}

projectZomboid {
    gamePath.set(pzDirectory)
    serverPath.set(pzDirectory)
}

dependencies {
    compileOnly(pzApi())
    implementation("net.bytebuddy:byte-buddy:1.12.6")
    implementation("net.bytebuddy:byte-buddy-agent:1.12.6")
    implementation("io.javalin:javalin:4.1.1")
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("org.aspectj:aspectjrt:1.8.9")
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("io.micrometer:micrometer-core:latest.release")
    implementation("io.micrometer:micrometer-registry-prometheus:latest.release")
}

tasks.register<ProjectZomboidLaunchTask>("pzServer64") {
    launchType.set(SERVER)
    additionalJvmArgs.set(
        listOf(
            "-javaagent:${shadowJar.outputs.files.asPath}",
        )
    )

    dependsOn(shadowJar)
}

tasks.getByName<Jar>("jar") {
    archiveFileName.set("pz-agent-lib.jar")
}

val shadowJar = tasks.getByName<ShadowJar>("shadowJar") {
    manifest {
        attributes["Premain-Class"] = "com.github.garyttierney.zomboid.server.ZomboidServerPatcher"
        attributes["Can-Retransform-Classes"] = true
        attributes["Can-Redefine-Classes"] = true
    }

    archiveFileName.set("pz-agent.jar")
    relocate("net.bytebuddy", "com.github.garyttierney.zomboid.bundled.bytebuddy")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
    }
}