package sample

import com.pinterest.ktlint.core.Rule
import com.pinterest.ktlint.core.ast.children
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes
import java.io.File

class CheckExistTestClassRule : Rule("check-exist-test-class") {
    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType == KtStubElementTypes.CLASS) {
            val children = node.children()
            if (children.map { it.elementType }.contains(KtTokens.CLASS_KEYWORD).not()) return
            val className = children
                .filter { it.elementType == KtTokens.IDENTIFIER }
                .firstOrNull()
                ?.text
                ?: throw Exception("cannot find class name.")

            val testClassName = className + "Tests"

            if (Regex("""Tests$""").containsMatchIn(className).not() && hasTestClass(testClassName).not()) {
                emit(
                    node.startOffset,
                    "テストクラスが存在しません",
                    false
                )
            }
        }
    }
}

val testDirectoryPath = "/.../src/test/kotlin" // TODO

val testClassNames = run {
    val result = mutableListOf<String>()
    fun addTestClasses(startPath: String) {
        File(startPath).listFiles()?.forEach {
            when {
                it.isFile -> result.add(it.nameWithoutExtension)
                it.isDirectory -> addTestClasses(it.path)
            }
        }
    }

    addTestClasses(testDirectoryPath)
    result.toList()
}

fun hasTestClass(className: String) = testClassNames.contains(className)
