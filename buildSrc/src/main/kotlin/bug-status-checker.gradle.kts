import ru.vyarus.gradle.plugin.python.PythonExtension
import ru.vyarus.gradle.plugin.python.task.PythonTask

plugins {
    id("ru.vyarus.use-python")
}

python {
    pip("requests:2.28.1")

    scope = PythonExtension.Scope.USER
}

tasks.register<PythonTask>("checkBugStatuses") {
    group = "debugify"

    command = rootProject.file("scripts/check_bug_fixes.py").toString()
}
