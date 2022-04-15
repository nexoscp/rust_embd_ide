package rust.embd.toml

import com.intellij.codeInsight.completion.*
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiFile
import com.intellij.util.ProcessingContext
import org.toml.lang.TomlLanguage
import org.toml.lang.psi.TomlTokenType

private val PsiFile.isEmbedConfigFile: Boolean
    get() {
        return this.fileType == FileType.REFERENCE
    }

class EmbedTomlCompletion : CompletionContributor() {
    init {
        val provider = EmbedTOMLCompletionProvider()
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(TomlTokenType("")).withLanguage(TomlLanguage),
            provider )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(TomlTokenType("")).withLanguage(TomlLanguage),
            provider )
    }
}

class EmbedTOMLCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(completionParameters: CompletionParameters, processingContext: ProcessingContext, resultSet: CompletionResultSet) {
        if(completionParameters.originalFile.isEmbedConfigFile) {

        }
       // Section.sections.forEach { resultSet.addElement(LookupElementBuilder.create(it)) }
    }
}

// see https://github.com/probe-rs/cargo-embed/blob/master/src/config/default.toml
object Sections {
    val sufixe = listOf(Section("probe"), Section("flashing"), Section("reset"), Section("general"), Section("rtt"), Section("gdb"))
}

class Section(name: String)
