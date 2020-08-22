package rust.embd.project

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.facet.ui.ValidationResult
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.CustomStepProjectGenerator
import com.intellij.ide.util.projectWizard.ProjectSettingsStepBase
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.impl.welcomeScreen.AbstractActionWithPanel
import com.intellij.platform.DirectoryProjectGenerator
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.platform.WebProjectGenerator
import com.intellij.ui.content.ContentManager
import java.io.File
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class Generator: DirectoryProjectGenerator<Settings>, CustomStepProjectGenerator<Settings> {
    override fun getName() = Plugin.name

    override fun getLogo() = Plugin.icon

    override fun createPeer() = Peer()

    override fun generateProject(project: Project, baseDir: VirtualFile, settings: Settings, module: Module) {
        getApplication().runWriteAction { init(project, baseDir, settings, module); }
    }

    override fun validate(baseDirPath: String): ValidationResult = ValidationResult.OK
    override fun createStep(projectGenerator: DirectoryProjectGenerator<Settings>?, callback: AbstractNewProjectStep.AbstractCallback<Settings>?): AbstractActionWithPanel {
        return ProjectSettingsStepBase<Settings>(projectGenerator, callback)
    }
}

class Peer : ProjectGeneratorPeer<Settings> {

    override fun validate(): ValidationInfo? {
       // return if (ServiceManager.getService(*::class.java).isAvailable()) null else ValidationInfo("No * CLI found")
        return null
    }

    override fun getSettings(): Settings {
        return Settings()
    }

    override fun addSettingsStateListener(listener: WebProjectGenerator.SettingsStateListener) {
    }

    override fun buildUI(settingsStep: SettingsStep) {
    }

    override fun isBackgroundJobRunning(): Boolean {
        return false
    }

    override fun getComponent(): JComponent {
        return JLabel("empty")
    }
}

fun init(project: Project, baseDir: VirtualFile, settings: Settings, module: Module) {
    val init = GeneralCommandLine("cargo", "init")
    init.workDirectory = File(project.basePath)
    val processHandler = OSProcessHandler(init)

    processHandler.addProcessListener(object : ProcessListener {
        override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {}

        override fun processTerminated(event: ProcessEvent) {
            VirtualFileManager.getInstance().asyncRefresh(null)
        }

        override fun processWillTerminate(event: ProcessEvent, willBeDestroyed: Boolean) {}

        override fun startNotified(event: ProcessEvent) {}
    })

    val console = TextConsoleBuilderFactory
            .getInstance()
            .createBuilder(project)
            .console
    console.attachToProcess(processHandler)
    val contentManager: ContentManager? = ToolWindowManager
            .getInstance(project)
            .getToolWindow("Console")
            ?.contentManager
    val content = contentManager?.factory?.createContent(console.component, "cargo init", false)
    if (content != null) {
        contentManager.addContent(content)
    }
    processHandler.startNotify()
}

