import com.android.build.gradle.api.BaseVariant
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

            // 创建自定义任务 , 并且设置该任务依赖于其它任务
            MyTask myTask = project.tasks.create('HelloMyTask', MyTask)
            // 设置自定义任务依赖于 preBuild 任务
            // 执行 HelloMyTask 自定义任务之前 , 需要先执行 preBuild 任务
            myTask.dependsOn project.tasks.getByName('preBuild')

            // 设置 preDebugBuild 任务 依赖于 HelloMyTask 自定义任务
            // 执行 preDebugBuild 任务之前 , 必须先执行 HelloMyTask 任务
            project.tasks.getByName('preDebugBuild').dependsOn myTask

            // 按照上述配置 , HelloMyTask 任务的执行 需要在 preBuild 与 preDebugBuild 任务之间

            // 获取 Android Gradle 的一系列配置
            project.android.applicationVariants.all {
                BaseVariant variant ->
                    println "project.android.applicationVariants : " + variant.description
            }

        }
    }
}