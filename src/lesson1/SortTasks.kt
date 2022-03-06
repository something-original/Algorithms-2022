@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.lang.IllegalArgumentException


/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
//Трудоемкость - O(NlogN), ресурсоемкость - O(N)
fun sortTimes(inputName: String, outputName: String) {

    File(outputName).bufferedWriter().use { writer ->
        val timesList = mutableListOf<Pair<Int, String>>()
        for (line in File(inputName).readLines()) {
            if (!line.matches(Regex("""((0[0-9]|1[0-2]):[0-5][0-9]:[0-5][0-9]) (AM|PM)"""))) {
                throw IllegalArgumentException("Неверный формат данных")
            } else {
                val list1 = line.split(" ")
                val list2 = list1[0].split(":").map { it.toInt() }
                var seconds: Int = if (list1[1] == "AM") {
                    if (list2[0] == 12) 0
                    else list2[0] * 3600
                } else {
                    if (list2[0] == 12) list2[0] * 3600
                    else (list2[0] + 12) * 3600
                }
                seconds += (list2[1] * 60 + list2[2])
                timesList.add(Pair(seconds, line))
            }
        }
        timesList.sortBy { it.first }
        for (pair in timesList) {
            writer.write(pair.second + "\n")
        }
    }
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
//Трудоемкость - O(NlogN), ресурсоемкость - O(N)
fun sortTemperatures(inputName: String, outputName: String) {
    File(outputName).bufferedWriter().use {
        val temperatures = mutableListOf<Double>()
        val x = IllegalArgumentException()
        for (line in File(inputName).readLines()) {
            try {
                val temp = line.toDouble()
                if (temp >= -273.0 && temp <= 500.0) temperatures.add(temp)
                else throw x
            } catch (e: IllegalArgumentException) {
            }
        }
        val result = temperatures.sorted()
        for (tmp in result) {
            it.write(tmp.toString() + "\n")
        }
    }
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
//Трудоемкость - O(N), ресурсоемкость - O(N)
fun sortSequence(inputName: String, outputName: String) {

    File(outputName).bufferedWriter().use {
        val elemToFrequency = mutableMapOf<String, Int>()
        for (line in File(inputName).readLines()) {
            if (elemToFrequency[line] != null) {
                var tmp = elemToFrequency[line]!!
                tmp++
                elemToFrequency[line] = tmp
            } else elemToFrequency[line] = 1
        }
        val maxFrequency = elemToFrequency.values.maxOrNull()
        val maxFrequentElements = mutableListOf<Int>()
        for (entry in elemToFrequency.entries) {
            if (entry.value == maxFrequency) maxFrequentElements.add(entry.key.toInt())
        }
        val minFromMaxFrequent = maxFrequentElements.minOrNull()
        for (l in File(inputName).readLines()) {
            if (l != minFromMaxFrequent.toString()) it.write(l + "\n")
        }
        if (maxFrequency != null) {
            for (i in 1..maxFrequency) {
                if (minFromMaxFrequent != null) {
                    it.write(minFromMaxFrequent.toString() + "\n")
                }
            }
        }
    }
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

