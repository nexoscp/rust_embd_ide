import org.junit.Test

class Foo {
    @Test
    fun test() {
        val content = Foo::class.java.getResourceAsStream("chips.txt").bufferedReader()
        //content.copyTo(System.out.bufferedWriter())
        val variants = mutableListOf<String>()
        content.forEachLine { line ->
            if (line.startsWith("    ")) {
                // Variants:
                variants.clear()
            } else if (line.startsWith("        ")) {
                variants.add(line.trim())
            } else {
                // Series

            }}
    }
}