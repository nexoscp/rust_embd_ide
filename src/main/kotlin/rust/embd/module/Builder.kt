package rust.embd.module

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.application.ApplicationManager.getApplication
import com.intellij.openapi.roots.ModifiableRootModel
import rust.embd.project.generator.Init

/**
 *  @see https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000055950-how-can-you-add-items-to-CLion-s-New-Project-window
 */
class Builder() : ModuleBuilder() {
    companion object {
        val INSTANCE = Builder()
    }
    override fun getModuleType(): Type {
        return Type.INSTANCE
    }

    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val root = doAddContentEntry(modifiableRootModel)?.file ?: return //FIXME proper error handling
        modifiableRootModel.inheritSdk()
        Init(modifiableRootModel.project, root, getApplication()).run()
    }
}
