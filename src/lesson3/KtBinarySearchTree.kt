package lesson3

import java.lang.IllegalStateException
import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max

// attention: Comparable is supported but Comparator is not
class KtBinarySearchTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    class Node<T>(
        val value: T
    ) {
        var left: Node<T>? = null
        var right: Node<T>? = null
        var parent: Node<T>? = null
    }

    private var root: Node<T>? = null

    override var size = 0
        private set


    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }


    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    /**
     * Добавление элемента в дерево
     *
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     *
     * Спецификация: [java.util.Set.add] (Ctrl+Click по add)
     *
     * Пример
     */
    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        if (closest != null) {
            newNode.parent = closest//Ссылка на родительский элемент
        }
        return true
    }

    /**
     * Удаление элемента из дерева
     *
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: [java.util.Set.remove] (Ctrl+Click по remove)
     * (в Котлине тип параметера изменён с Object на тип хранимых в дереве данных)
     *
     * Средняя
     */

    //Вспомогательный метод для смены узлов, проверяет,
    //был ли элемент левым или правым у родителя и меняет ссылки между элементами
    private fun swap(parent: Node<T>, child: Node<T>?) {
        if (parent == root) root = child
        else {
            if (parent == parent.parent!!.left) parent.parent!!.left = child
            else parent.parent!!.right = child
        }
        if (child != null) {
            parent.parent = child
        }
    }

    //Трудоемкость - O(log(N)), ресурсоемкость - O(1)
    override fun remove(element: T): Boolean {
        val elem = find(element) ?: return false
        if (elem.value != element) return false
        if (elem.left == null) swap(elem, elem.right)
        else {
            if (elem.right == null) swap(elem, elem.left)
            if (elem.right != null && elem.left != null) {
                var toSwap = elem.right!!
                while (toSwap.left != null) {
                    toSwap = toSwap.left!!
                }
                if (toSwap != elem.right) {
                    swap(toSwap, toSwap.right)
                    toSwap.right = elem.right
                    toSwap.right!!.parent = toSwap
                }
                swap(elem, toSwap)
                toSwap.left = elem.left
                toSwap.left!!.parent = toSwap
            }
        }
        size--
        return true
    }


    override fun comparator(): Comparator<in T>? =
        null

    override fun iterator(): MutableIterator<T> =
        BinarySearchTreeIterator()

    inner class BinarySearchTreeIterator internal constructor() : MutableIterator<T> {

        private val stack = Stack<Node<T>>()
        var current: Node<T>? = null

        init {
            addTheNode(root)
        }

        private fun addTheNode(node: Node<T>?) {
            if (node != null) {
                stack.push(node)
                addTheNode(node.left)
            }
        }

        /**
         * Проверка наличия следующего элемента
         *
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         *
         * Спецификация: [java.util.Iterator.hasNext] (Ctrl+Click по hasNext)
         *
         * Средняя
         */
        //Трудоемкость - O(1), ресурсоемкость - O(1)
        override fun hasNext(): Boolean = stack.isNotEmpty()


        /**
         * Получение следующего элемента
         *
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         *
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         *
         * Спецификация: [java.util.Iterator.next] (Ctrl+Click по next)
         *
         * Средняя
         */
        //Трудоемкость - O(1), ресурсоемкость - O(1)
        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            val node = stack.pop()
            current = node
            addTheNode(node.right)
            return node.value
        }

        /**
         * Удаление предыдущего элемента
         *
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         *
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         *
         * Спецификация: [java.util.Iterator.remove] (Ctrl+Click по remove)
         *
         * Сложная
         */
        //Трудоемкость - O(log(N)), ресурсоемкость - O(1)
        override fun remove() {
            if (current == null) throw IllegalStateException()
            removeCurrent(current!!)
            current = null
        }

        private fun removeCurrent(elem: Node<T>) {
            if (elem.left == null) swap(elem, elem.right)
            else {
                if (elem.right == null) swap(elem, elem.left)
                if (elem.right != null && elem.left != null) {
                    var toSwap = elem.right!!
                    while (toSwap.left != null) {
                        toSwap = toSwap.left!!
                    }
                    if (toSwap != elem.right) {
                        swap(toSwap, toSwap.right)
                        toSwap.right = elem.right
                        toSwap.right!!.parent = toSwap
                    }
                    swap(elem, toSwap)
                    toSwap.left = elem.left
                    toSwap.left!!.parent = toSwap
                }
            }
            size--
        }
    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: [java.util.SortedSet.subSet] (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: [java.util.SortedSet.headSet] (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     *
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     *
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     *
     * Спецификация: [java.util.SortedSet.tailSet] (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     *
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }

    override fun height(): Int =
        height(root)

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

}