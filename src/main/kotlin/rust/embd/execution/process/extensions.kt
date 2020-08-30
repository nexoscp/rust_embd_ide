package rust.embd.execution.process

import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.ui.ConsoleView

fun OSProcessHandler.withProcessListener(p: ProcessListener): OSProcessHandler {
    addProcessListener(p)
    return this
}

fun ConsoleView.withAttachToProcess(processHandler: ProcessHandler): ConsoleView {
    attachToProcess(processHandler)
    return this
}
