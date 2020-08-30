package rust.embd.project.generator

import com.intellij.facet.ui.ValidationResult
import com.intellij.ide.util.projectWizard.AbstractNewProjectStep
import com.intellij.ide.util.projectWizard.CustomStepProjectGenerator
import com.intellij.ide.util.projectWizard.ProjectSettingsStepBase
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.impl.welcomeScreen.AbstractActionWithPanel
import com.intellij.platform.DirectoryProjectGenerator
import com.intellij.platform.ProjectGeneratorPeer
import com.intellij.platform.WebProjectGenerator
import rust.embd.Plugin
import rust.embd.project.Settings
import javax.swing.JComponent

class Generator: DirectoryProjectGenerator<Settings>, CustomStepProjectGenerator<Settings> {
    override fun getName() = Plugin.name

    override fun getLogo() = Plugin.icon

    override fun createPeer() = Peer(getApplication())

    override fun generateProject(project: Project, baseDir: VirtualFile, settings: Settings, module: Module) {
        getApplication().runWriteAction(Init(project, baseDir, getApplication()))
    }

    override fun validate(baseDirPath: String): ValidationResult = ValidationResult.OK
    override fun createStep(projectGenerator: DirectoryProjectGenerator<Settings>?, callback: AbstractNewProjectStep.AbstractCallback<Settings>?): AbstractActionWithPanel {
        return ProjectSettingsStepBase<Settings>(projectGenerator, callback)
    }
}

class Peer(private val application: Application) : ProjectGeneratorPeer<Settings> {
    private val settings = Settings()
    private val panel = Panel()
    init {
 //       application.runWriteAction(ListChips(panel, application))
    }

    override fun validate(): ValidationInfo? {
       // return if (ServiceManager.getService(*::class.java).isAvailable()) null else ValidationInfo("No * CLI found")
        return null
    }

    override fun getSettings(): Settings {
        return settings
    }

    override fun addSettingsStateListener(listener: WebProjectGenerator.SettingsStateListener) {
    }

    override fun buildUI(settingsStep: SettingsStep) {
    }

    override fun isBackgroundJobRunning(): Boolean {
        return false
    }

    override fun getComponent(): JComponent {
        return panel
    }
}