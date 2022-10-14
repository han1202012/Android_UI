package kim.hsl.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class Plugin4 implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println 'Plugin4'
    }
}