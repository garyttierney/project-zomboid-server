import com.github.garyttierney.zomboid.ProjectZomboidExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec


inline val PluginDependenciesSpec.`project-zomboid`: PluginDependencySpec
    get() = id("com.github.garyttierney.zomboid")

fun Project.pzApi() = files(the<ProjectZomboidExtension>().gamePath.get())