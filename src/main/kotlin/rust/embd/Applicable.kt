package rust.embd

import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil.findChildrenOfType
import org.toml.lang.psi.TomlTable

object Applicable {
    fun isApplicable(project: Project): Boolean {
        return ModuleManager.getInstance(project).modules.fold(false){ found, module -> inspectModule(found, module, project)}
    }

    private fun inspectModule(found: Boolean, module: Module, project: Project):Boolean {
        if (found) return true
        val rootManager = ModuleRootManager.getInstance(module)
        return rootManager.contentRoots.fold(false) { f, contentRoot -> inspectContentRoot(f, contentRoot, project) }
    }

    private fun inspectContentRoot(found: Boolean, contentRoot: VirtualFile, project: Project):Boolean {
        if(found) return true;
        return contentRoot.children.fold(false) { f, file -> isCargoFile(f, file, project) }
    }

    private fun isCargoFile(found: Boolean, file: VirtualFile, project: Project):Boolean {
        if(found) return true
        if( file.name == "Cargo.toml" ) {
            val psi = PsiManager.getInstance(project).findFile(file)
            //return findChildrenOfType(psi, TomlTable::class.java).fold(false) { found, table -> false}
            return true;
        }
        return false
    }
}