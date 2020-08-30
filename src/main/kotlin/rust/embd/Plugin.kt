package rust.embd

import com.intellij.icons.AllIcons
import com.intellij.openapi.externalSystem.model.ProjectSystemId
import javax.swing.Icon
import com.intellij.openapi.diagnostic.Logger;

object Plugin {
    const val description = "Rust Embedded"
    const val name = "Rust Embedded"
    val ID = ProjectSystemId(name.replace(' ', '_').toLowerCase())
    val icon: Icon = AllIcons.General.Information
    val log = Logger.getInstance("rust:embd")
}