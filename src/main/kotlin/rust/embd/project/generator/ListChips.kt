package rust.embd.project.generator

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.application.Application
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.io.FileUtilRt.createTempDirectory
import rust.embd.execution.process.withProcessListener
import java.io.File

// cargo install probe-run
// probe-run --list-chips
class ListChips(private val log: Appendable, private val application: Application) : Runnable {
    override fun run() {
        val tmp = createTempDirectory("cargo", "foo")
        val cmd = GeneralCommandLine("cargo", "install", "probe-run")
                .withWorkDirectory(tmp)
        val processHandler = OSProcessHandler(cmd)
                .withProcessListener(object : ProcessAdapter() {
                    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                        log.append(event.text)
                    }
                    override fun processTerminated(event: ProcessEvent) {
                        listChips(tmp)
                    }
                })

        processHandler.startNotify()
    }

    private fun listChips(tmp: File) {
        val cmd = GeneralCommandLine("probe-run", "--list-chips")
                .withWorkDirectory(tmp)
        val processHandler = OSProcessHandler(cmd)
                .withProcessListener(object : ProcessAdapter() {
                    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                        application.invokeLater {log.append(event.text)}
                    }
                    override fun processTerminated(event: ProcessEvent) {

                    }
                })
        processHandler.startNotify()
    }
}
