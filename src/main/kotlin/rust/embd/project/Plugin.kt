package rust.embd.project

import com.intellij.icons.AllIcons
import com.intellij.openapi.externalSystem.model.ProjectSystemId
import javax.swing.Icon

object Plugin {
    const val name = "Rust Embedded"
    val ID = ProjectSystemId(name.replace(' ', '_').toLowerCase())
    val icon: Icon = AllIcons.General.Information
}