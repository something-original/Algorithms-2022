package lesson5

import java.lang.IllegalStateException

/**
 * Множество(таблица) с открытой адресацией на 2^bits элементов без возможности роста.
 */
class KtOpenAddressingSet<T : Any>(private val bits: Int) : AbstractMutableSet<T>() {
    init {
        require(bits in 2..31)
    }

    private val capacity = 1 shl bits

    private val storage = Array<Any?>(capacity) { null }

    override var size: Int = 0

    private object Removed //специальный тип, который указывает на то, что эл-т был удален


    /**
     * Индекс в таблице, начиная с которого следует искать данный элемент
     */
    private fun T.startingIndex(): Int {
        return hashCode() and (0x7FFFFFFF shr (31 - bits))
    }

    /**
     * Проверка, входит ли данный элемент в таблицу
     */
    override fun contains(element: T): Boolean {
        var index = element.startingIndex()
        var current = storage[index]
        while (current != null) {
            if (current == element) {
                return true
            }
            index = (index + 1) % capacity
            current = storage[index]
        }
        return false
    }

    /**
     * Добавление элемента в таблицу.
     *
     * Не делает ничего и возвращает false, если такой же элемент уже есть в таблице.
     * В противном случае вставляет элемент в таблицу и возвращает true.
     *
     * Бросает исключение (IllegalStateException) в случае переполнения таблицы.
     * Обычно Set не предполагает ограничения на размер и подобных контрактов,
     * но в данном случае это было введено для упрощения кода.
     */
    override fun add(element: T): Boolean {
        val startingIndex = element.startingIndex()
        var index = startingIndex
        var current = storage[index]
        while (current != null && current != Removed) { //при добавлении ячейка с маркером Removed рассматривается
            if (current == element) {                   //как свободная
                return false
            }
            index = (index + 1) % capacity
            check(index != startingIndex) { "Table is full" }
            current = storage[index]
        }
        storage[index] = element
        size++
        return true
    }

    /**
     * Удаление элемента из таблицы
     *
     * Если элемент есть в таблице, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     *
     * Спецификация: [java.util.Set.remove] (Ctrl+Click по remove)
     *
     * Средняя
     */
    //Трудоемкость - О(N), ресурсоемкость - О(1)
    override fun remove(element: T): Boolean {
        val startIndex = element.startingIndex()
        var index = startIndex
        var current = storage[index]
        while (current != null) {
            if (current == element) {
                storage[index] = Removed
                size--
                return true
            }
            index = (index + 1) % capacity
            if (index == startIndex) return false
            current = storage[index]
        }
        return false
    }

    /**
     * Создание итератора для обхода таблицы
     *
     * Не забываем, что итератор должен поддерживать функции next(), hasNext(),
     * и опционально функцию remove()
     *
     * Спецификация: [java.util.Iterator] (Ctrl+Click по Iterator)
     *
     * Средняя (сложная, если поддержан и remove тоже)
     */
    override fun iterator(): MutableIterator<T> {
        return AddressingSetIterator()
    }

    inner class AddressingSetIterator : MutableIterator<T> {

        private var currentInd = 0
        private var deleteInd = -1

        //Трудоемкость - О(N) (в худшем случае), ресурсоемкость - О(1)
        override fun hasNext(): Boolean {
            if (size == 0) return false
            while (currentInd < capacity && (storage[currentInd] == null || storage[currentInd] == Removed)) {
                currentInd++
            }
            return currentInd < capacity
        }

        //Трудоемкость - О(N) (в худшем случае), ресурсоемкость - О(1)
        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            deleteInd = currentInd
            val currentObject = storage[currentInd]
            currentInd++
            return currentObject as T
        }

        //Трудоемкость и ресурсоемкость - О(1)
        override fun remove() {
            if (deleteInd == -1 || storage[deleteInd] == null) throw IllegalStateException()
            storage[deleteInd] = Removed
            size--
        }

    }
}
