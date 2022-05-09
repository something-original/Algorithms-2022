package lesson5

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class KtOpenAddressingSetTest : AbstractOpenAddressingSetTest() {

    override fun <T : Any> create(bits: Int): MutableSet<T> {
        return KtOpenAddressingSet(bits)
    }

    @Test
    @Tag("Example")
    fun addTest() {
        doAddTest()
    }

    @Test
    @Tag("7")
    fun removeTest() {
        doRemoveTest()
    }

    @Test //my test
    fun myRemoveTest() {
        doMyRemoveTest()
    }

    @Test
    @Tag("5")
    fun iteratorTest() {
        doIteratorTest()
    }

    @Test //my test
    fun myIteratorTest() {
        doMyIteratorTest()
    }

    @Test
    @Tag("8")
    fun iteratorRemoveTest() {
        doIteratorRemoveTest()
    }

    @Test //my test
    fun myIteratorRemoveTest() {
        doMyIteratorRemoveTest()
    }
}