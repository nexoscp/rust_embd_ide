package rust.embd.project.generator

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.filters.TextConsoleBuilder
import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.application.Application
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import rust.embd.execution.process.withAttachToProcess
import rust.embd.execution.process.withProcessListener
import rust.embd.Plugin
import rust.embd.toolwindow.console.ID
import rust.embd.vfs.makeDirectory
import java.io.IOException
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files.write
import java.nio.file.StandardOpenOption.APPEND

// TODO  cargo install cargo-binutils?
class Init(
        private val project: Project,
        private val baseDir: VirtualFile,
        private val application: Application) : Runnable {
    override fun run() {
        val toolWindow = ToolWindowManager
                .getInstance(project)
                .getToolWindow(ID)
        if (toolWindow == null) {
            Plugin.log.error("Missing ToolWindow with ID '${ID}', Add <toolWindow id=\"${ID}\" ...> to plugin.xml")
        }
        val consoleBuilder = TextConsoleBuilderFactory
                .getInstance()
                .createBuilder(project)

        val init = GeneralCommandLine("cargo", "init")
                .withWorkDirectory(baseDir.path)
        val processHandler = OSProcessHandler(init)
                .withProcessListener(object : ProcessAdapter() {
                    override fun processTerminated(event: ProcessEvent) {
                        //Application#writeAction must be called from UI Thread
                         application.invokeLater {writeFiles(baseDir) }
                        application.invokeLater { application.runWriteAction {installTarget(baseDir, toolWindow, consoleBuilder)} }
                        application.invokeLater { writeCargoDependency(baseDir)}
                    }
                })

        val console = consoleBuilder
                .console
                .withAttachToProcess(processHandler)
                val contentManager = toolWindow?.contentManager
        val content = contentManager?.factory?.createContent(console.component, "cargo init", false)
        if (content != null) {
            contentManager.addContent(content)
        }
        processHandler.startNotify()
    }

    private fun installTarget(baseDir: VirtualFile, toolWindow: ToolWindow?, consoleBuilder: TextConsoleBuilder) {
        val init = GeneralCommandLine("rustup", "target", "add", "thumbv7em-none-eabi")
                .withWorkDirectory(baseDir.path)
        val processHandler = OSProcessHandler(init)
                .withProcessListener(object : ProcessAdapter() {
                    override fun processTerminated(event: ProcessEvent) {
                        Plugin.log.info("installed target")
                    }
                })
       val console = consoleBuilder
                .console
                .withAttachToProcess(processHandler)
        val contentManager = toolWindow?.contentManager
        val content = contentManager?.factory?.createContent(console.component, "install target", false)
        if (content != null) {
            contentManager.addContent(content)
        }
        processHandler.startNotify()
    }

    private fun writeFiles(baseDir: VirtualFile) {
       application.runWriteAction {
            try {
                baseDir
                        .makeDirectory(this, "src")
                        .findOrCreateChildData(this, "main.rs")
                        .getOutputStream(this)
                        .bufferedWriter()
                        .use {
                            it.write("""
                    #![no_std]
                    use cortex_m::asm;
                    use cortex_m_rt::entry;
                    use rtt_target::rprintln;

                    #[entry]
                    fn main() -> ! {
                        // omitted: rtt initialization
                        rprintln!("Hello, world!");
                        loop { asm::bkpt() }
                    }
                """.trimIndent())
                        }
            } catch (e: IOException) {
                Plugin.log.error(e)
            }
            VirtualFileManager.getInstance().asyncRefresh(null)
        }

      application.runWriteAction {
            try {
                baseDir
                        .makeDirectory(this, ".cargo")
                        .findOrCreateChildData(this, "config.toml")
                        .getOutputStream(this)
                        .bufferedWriter()
                        .use {
                            it.write("""
                    [target.thumbv7em-none-eabi]
                    runner = "probe-run --chip nRF52840_xxAA"

                    [build]
                    target = "thumbv7em-none-eabi"
                """.trimIndent())
                        }
            } catch (e: IOException) {
                Plugin.log.error(e)
            }
            VirtualFileManager.getInstance().asyncRefresh(null)
        }
    }

    private fun writeCargoDependency(baseDir: VirtualFile){
        application.runWriteAction {
            try {
                val p = baseDir
                        .findOrCreateChildData(this, "Cargo.toml")
                        .toNioPath();
                val depenedencies : String = """
                    cortex-m = "0.6.3"
                    cortex-m-rt = "0.6.12"
                    rtt-target = "0.2.2"
                """.trimIndent()
              write(p , listOf(depenedencies), UTF_8, APPEND)
            } catch (e: IOException) {
                Plugin.log.error(e)
            }
            VirtualFileManager.getInstance().asyncRefresh(null)
        }
    }
}
