import org.junit.Test

class Foo {
    @Test
    fun test() {
        val content = Foo::class.java.getResourceAsStream("chips.txt").bufferedReader()
        val variants = mutableListOf<String>()
        content.forEachLine { line ->
            when {
                line.startsWith("    ") -> {
                    // Variants:
                    variants.clear()
                }
                line.startsWith("        ") -> {
                    variants.add(line.trim())
                }
                else -> {
                    // Series

                }
            }
        }
    }
}