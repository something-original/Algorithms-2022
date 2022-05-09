package lesson4

import java.util.*

/**
 * Префиксное дерево для строк
 */
class KtTrie : AbstractMutableSet<String>(), MutableSet<String> {

    private class Node {
        val children: SortedMap<Char, Node> = sortedMapOf()
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

        private val values = ArrayDeque<MutableIterator<Map.Entry<Char, Node>>>()
        private var current = ""
        private var cnt = 0

        init {
            values.push(root.children.entries.iterator())
        }

        //Трудоемкость - О(1), ресурсоемкость - О(1)
        override fun hasNext(): Boolean {
            return size > cnt
        }

        //Трудоемкость - О(N), ресурсоемкость - О(H), где H - длина самого длинного слова
        override fun next(): String {
            if (!hasNext()) throw NoSuchElementException()
            iterate()
            return current
        }

        private fun iterate() {
            var iterator = values.peek()
            while (iterator != null) {
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    val key = next.key
                    val value = next.value
                    if (key == 0.toChar()) {
                        cnt++
                        return
                    }
                    iterator = value.children.entries.iterator()
                    values.push(iterator)
                    current += key.toString()
                }
                values.pop()
                if (current.isNotBlank()) {
                    current = current.dropLast(1)
                }
                iterator = values.peek()
            }
        }

        //Трудоемкость - О(1), ресурсоемкость - О(1)
        override fun remove() {
            if (current.isBlank()) throw IllegalStateException()
            if (values.peek() != null) {
                values.peek().remove()
                cnt--
                size--
                current = ""
            }
        }
    }
}