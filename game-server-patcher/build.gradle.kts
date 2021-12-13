import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.JavaVersion.VERSION_11

plugins {
    java
    id ("com.github.johnrengelman.shadow") version ("7.1.0")
}

group = "org.example"
version = "1.0-SNAPSHOT"

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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
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