package rust.embd.vfs

import com.intellij.core.CoreBundle
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException

@Throws(IOException::class)
fun VirtualFile.makeDirectory(requestor: Any, name: String): VirtualFile {
    if (!isDirectory) {
        throw IOException(CoreBundle.message("directory.create.wrong.parent.error"))
    }
    if (!isValid) {
        throw IOException(CoreBundle.message("invalid.directory.create.files"))
    }
    if (!fileSystem.isValidName(name)) {
        throw IOException(CoreBundle.message("directory.invalid.name.error", name))
    }
    val child = findChild(name)
    if (child != null) {
        return child
    }

    return createChildDirectory(requestor, name)
}