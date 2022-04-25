package lesson4

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class KtTrieTest : AbstractTrieTest() {

    override fun create(): MutableSet<String> =
        KtTrie()

    @Test
    @Tag("Example")
    fun generalTest() {
        doGeneralTest()
    }

    @Test
    @Tag("7")
    fun iteratorTest() {
        doIteratorTest()
    }

    @Test//my test
    fun myIteratorTest() {
        doMyIteratorTest()
    }

    @Test
    @Tag("8")
    fun iteratorRemoveTest() {
        doIteratorRemoveTest()
    }

    @Test//my test
    fun myIteratorRemoveTest() {
        doMyIteratorRemoveTest()
    }
}