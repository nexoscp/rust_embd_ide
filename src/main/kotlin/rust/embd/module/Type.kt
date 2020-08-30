package rust.embd.module

import com.intellij.openapi.module.ModuleType
import rust.embd.Plugin
import javax.swing.Icon

/**
 *  @see https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000055950-how-can-you-add-items-to-CLion-s-New-Project-window
*/
class Type : ModuleType<Builder>(Plugin.ID.id) {
    companion object {
        val INSTANCE = Type()
    }
    override fun createModuleBuilder(): Builder {
        return Builder.INSTANCE
    }

    override fun getName(): String {
        return Plugin.name
    }

    override fun getDescription(): String {
        return Plugin.description
    }

    override fun getNodeIcon(isOpened: Boolean): Icon {
        return Plugin.icon
    }
}
