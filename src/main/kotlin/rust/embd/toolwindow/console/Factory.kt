package rust.embd.toolwindow.console

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import rust.embd.Applicable

class Factory : ToolWindowFactory {
    override fun isApplicable(project: Project) = Applicable.isApplicable(project)
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {}
}

const val ID = "Console"