@file:Suppress("UNUSED_PARAMETER")

package lesson2

import kotlin.math.floor
import kotlin.math.sqrt

/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    TODO()
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 *
 * Общий комментарий: решение из Википедии для этой задачи принимается,
 * но приветствуется попытка решить её самостоятельно.
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    TODO()
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 */
//Трудоемкость - O(M*N), ресурсоемкость - O(M*N), где M и N - длины строк
fun longestCommonSubstring(first: String, second: String): String {
    var result = ""
    if (first == second) return first
    if (first == "" || second == "") return result
    val matrix: Array<Array<Int>> = Array(first.length + 1) { Array(second.length + 1) { 0 } }
    var maxLength = 0
    var lastIndOfFirstSubstring = 0
    for (i in 1 until matrix.size) {
        matrix[i] = Array(second.length + 1) { 0 }
        for (j in 1 until matrix[i].size) {
            if (first[i - 1] == second[j - 1]) {
                if (i != 1 && j != 1) matrix[i][j] = matrix[i - 1][j - 1] + 1
                else matrix[i][j] = 1
                if (matrix[i][j] > maxLength) {
                    maxLength = matrix[i][j]
                    lastIndOfFirstSubstring = i
                }
            }
        }
    }
    if (maxLength != 0) result = first.substring(lastIndOfFirstSubstring - maxLength, lastIndOfFirstSubstring)
    return result
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */

//Трудоемкость -  O(N log(log N)), ресурсоемкость - O(N)
fun calcPrimesNumber(limit: Int): Int {
    var result = 0
    val sieveErat = Array(limit + 1) { true }
    if (limit <= 1) return 0
    if (limit == 2) return 1

    for (i in 2..floor(sqrt(limit.toDouble())).toInt()) {
        if (sieveErat[i]) {
            var j = i * i
            while (j <= limit) {
                sieveErat[j] = false
                j += i
            }
        }
    }

    for (i in 2..limit) {
        if (sieveErat[i]) result++
    }
    return result
}

