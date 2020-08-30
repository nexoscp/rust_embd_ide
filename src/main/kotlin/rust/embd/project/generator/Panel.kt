package rust.embd.project.generator

import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

class Panel : JPanel(BorderLayout()), Appendable {
    private val log = JTextArea()

    init {
        add(JScrollPane(log), BorderLayout.CENTER)
    }

    override fun append(csq: CharSequence?): Appendable {
        log.text = log.text + csq
        return this
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): Appendable {
        log.text = log.text + csq
        return this
    }

    override fun append(c: Char): Appendable {
        log.text = log.text + c
        return this
    }
}