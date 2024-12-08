package org.example.day05

import org.example.readAsString

fun main() {
//    val input = "day05/test_input".readAsString()
    val input = "day05/input".readAsString()

    val split = input.split(System.lineSeparator() + System.lineSeparator())
    val orderInput = split[0]
    val printInput = split[1]

    val orders = orderInput.split(System.lineSeparator()).map {
        val orders = it.split("|")
        orders[0].toInt() to orders[1].toInt()
    }

    val prints = printInput.split(System.lineSeparator()).map {
        it.split(",").map(String::toInt)
    }

    val correctPrints = prints.mapNotNull {
        val printValids = checkPrintValidity(it, orders)
        if (printValids.all { b -> b }) it else null
    }

    val result = correctPrints.fold(0) { acc, print ->
        acc + print[print.size / 2]
    }
    println(result)


    // Part 2

    val incorrectPrints = prints.mapNotNull {
        val printValids = checkPrintValidity(it, orders)
        if (printValids.contains(false)) it else null
    }

    val resultPart2 = incorrectPrints.parallelStream().map { ints ->
        var possibleOrders = orders.filter {
            ints.contains(it.first) && ints.contains(it.second)
        }
        possibleOrders = possibleOrders.filter { order ->
            possibleOrders.any { it.first == order.second } || possibleOrders.any { it.second == order.first }
        }

        lateinit var correctedPrint: List<Int>
        possibleOrders.forEach {
            fun buildOrderPermutation(current: Pair<Int, Int>, currentList: List<Int>): List<List<Any>> {
                val seconds = possibleOrders.filter { order -> order.first == current.second }
                if (seconds.isEmpty()) {
                    return listOf(currentList + current.second)
                }
                return seconds.map { s ->
                    buildOrderPermutation(s, currentList + s.first)
                }
            }

            val res = buildOrderPermutation(it, listOf(it.first))
            res.filterNestedList {
                checkPrintValidity(it, possibleOrders).all { b -> b }
            }.filter {
                it.size == ints.size
            }.filterNotNull().firstOrNull()?.let {
                correctedPrint = it
            }
        }
        correctedPrint[correctedPrint.size / 2]
    }.reduce { t, u -> u + t }.get()

    println(resultPart2)
}

private fun checkPrintValidity(
    it: List<Int>,
    orders: List<Pair<Int, Int>>
) = it.mapIndexed { index, print ->
    if (index == it.size - 1) {
        return@mapIndexed true
    }
    val order = orders.find { order -> order.first == print && order.second == it[index + 1] }
    order != null
}

fun List<Any>.filterNestedList(condition: (List<Int>) -> Boolean): List<List<Int>> {
    val rresult = mutableListOf<List<Int>>()
    for (item in this) {
        when (item) {
            is List<*> -> {
                if (item[0] is Int) {
                    if (condition(item as List<Int>)) {
                        rresult.add(item)
                    }
                }
                // Recursively check nested lists
                val result = (item as List<Any>).filterNestedList(condition)
                rresult.addAll(result)
            }

            else -> continue
        }
    }
    return rresult
}