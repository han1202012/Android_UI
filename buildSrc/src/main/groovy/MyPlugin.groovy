import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println 'MyPlugin'

        // 创建一个扩展
        // 类似于 Android Gradle 插件中的 android
        // 引入了 MyPlugin 插件后
        // 就可以使用 myplugin 配置块
        def myplugin = project.extensions.create("myplugin", MyPluginExtensions)

        // 为 MyPlugin 自定义插件的 myplugin 扩展定义 扩展 mypluginextension
        myplugin.extensions.create("mypluginextension", MyPluginExtensionsExtensions)

        // 获取 自定义 Gradle 插件的扩展属性 , 必须在 Gradle 分析完成之后才能进行 , 否则获取不到
        project.afterEvaluate {
            println project.myplugin.name
            println project.myplugin.age
            println project.myplugin.mypluginextension.name
            println project.myplugin.mypluginextension.age
        }
    }
}