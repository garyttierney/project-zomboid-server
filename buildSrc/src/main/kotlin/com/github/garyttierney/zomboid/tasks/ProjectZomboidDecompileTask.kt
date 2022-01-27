package com.github.garyttierney.zomboid.tasks

import com.github.garyttierney.zomboid.*
import com.google.gson.Gson
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.java.decompiler.main.Fernflower
import org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler
import java.io.File

abstract class ProjectZomboidDecompileTask : DefaultTask() {

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    init {
        outputs.upToDateWhen { true}
    }

    @InputDirectory
    fun getBinaryInputs(): File {
        val gameSettings = project.extensions.findByType<ProjectZomboidExtension>()
        val parent = gameSettings?.serverPath?.get()

        return File(parent)
    }

    init {
        outputDirectory.convention(project.layout.buildDirectory.dir("zomboid-decompiled"))
        outputs.dir(outputDirectory)
    }

    @TaskAction
    fun execute() {

        val gameSettings = project.extensions.findByType<ProjectZomboidExtension>()

        val parent = gameSettings?.gamePath?.get()
        val launchSettingsFile = File(parent, "ProjectZomboid64.json")
        val launchSettings = Gson().fromJson(launchSettingsFile.readText(), ProjectZomboidSettings::class.java)

        val classpath = launchSettings.classpath.toMutableList()
        val zomboidMainClasses = File(parent, classpath.removeAt(0))
        val zomboidClasspath = classpath.map { "-e=${File(parent, it)}" }.toTypedArray()

        ConsoleDecompiler.main(
            arrayOf(
                "-mpm=60",
                zomboidMainClasses.toString(),
                *zomboidClasspath,
                outputDirectory.get().toString()
            )
        )

    }
}