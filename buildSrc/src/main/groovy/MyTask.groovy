import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * 自定义 Gradle 任务
 */
class MyTask extends org.gradle.api.DefaultTask {

    @TaskAction
    void run() {
        println 'MyTask TaskAction'
    }
}