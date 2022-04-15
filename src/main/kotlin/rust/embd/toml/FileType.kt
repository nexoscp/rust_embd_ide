package rust.embd.toml

import com.intellij.icons.AllIcons.Icons
import com.intellij.openapi.fileTypes.FileType
import javax.swing.Icon

class FileType : FileType {
    companion object {
        //TODO also for json and yaml
      //  val fileNames = listOf("Embed.local.toml", ".embed.local.toml", "Embed.toml", ".embed.toml")
        val REFERENCE = FileType()
    }

    override fun getName(): String {
        return "Embed Config File"
    }

    override fun getDescription(): String {
        return "Embed config file"
    }

    override fun getDefaultExtension(): String {
        return "toml"
    }

    override fun getIcon(): Icon? {
        return Icons.Ide.MenuArrow
    }

    override fun isBinary(): Boolean {
        return false
    }
}
