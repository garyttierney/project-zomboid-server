package com.github.garyttierney.zomboid

import com.github.garyttierney.zomboid.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*


open class ProjectZomboidPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val config = project.extensions.create("projectZomboid", ProjectZomboidExtension::class)

        project.afterEvaluate {
            val java = project.extensions.findByType<JavaPluginExtension>()

            tasks
                .withType(ProjectZomboidLaunchTask::class)
                .forEach {
                    it.configureAfterEvaluate(config)
                }


            java?.sourceSets?.create("zomboid") {
                val decompileTask by project.tasks.register<ProjectZomboidDecompileTask>("projectZomboidSources") {

                }

                val sourcesJar = project.tasks.create<Jar>("projectZomboidSourcesJar") {
                    from(decompileTask.outputs.files)
                    include("*.java")
                    destinationDirectory.set(project.layout.buildDirectory.get().dir("pz-libs"))
                    archiveFileName.set("project-zomboid-sources.jar")
                }
            }
        }
    }
}
