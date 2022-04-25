package lesson4

import java.lang.IllegalStateException
import java.util.*

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
        val children: SortedMap<Char, Node> = sortedMapOf()
        var char: Char? = null
    }

    private val root = Node()

    override var size: Int = 0
        private set

    override fun clear() {
        root.children.clear()
        size = 0
    }

    private fun String.withZero() = this + 0.toChar()

    private fun findNode(element: String): Node? {
        var current = root
        for (char in element) {
            current = current.children[char] ?: return null
        }
        return current
    }

    override fun contains(element: String): Boolean =
        findNode(element.withZero()) != null

    override fun add(element: String): Boolean {
        var current = root
        var modified = false
        for (char in element.withZero()) {
            val child = current.children[char]
            if (child != null) {
                current = child
            } else {
                modified = true
                val newChild = Node()
                current.children[char] = newChild
                current = newChild
            }
            current.char = char
        }
        if (modified) {
            size++
        }
        return modified
    }

    override fun remove(element: String): Boolean {
        val current = findNode(element) ?: return false
        if (current.children.remove(0.toChar()) != null) {
            size--
            return true
        }
        return false
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    override fun iterator(): MutableIterator<String> {
        return TrieIterator()
    }

    inner class TrieIterator : MutableIterator<String> {

        init {
            iterate(root, "")
        }

        private var values = ArrayDeque<String>()
        private var current = ""

        private fun iterate(node: Node, elem: String) {
            for (child in node.children.entries) {
                if (child.key != 0.toChar()) {
                    iterate(child.value, elem + child.key)
                } else {
                    values.add(elem)
                }
            }
        }

        //Трудоемкость - O(1), ресурсоемкость - O(1)
        override fun hasNext(): Boolean {
            return (values.isNotEmpty())
        }

        //Трудоемкость - O(1), ресурсоемкость - O(1)
        override fun next(): String {
            if (!hasNext()) throw NoSuchElementException()
            current = values.pop()
            return current
        }

        //Трудоемкость - O(N), ресурсоемкость - O(N)
        override fun remove() {
            if (current.isNotBlank()) {
                remove(current)
                current = ""
            } else {
                throw IllegalStateException()
            }
        }
    }
}