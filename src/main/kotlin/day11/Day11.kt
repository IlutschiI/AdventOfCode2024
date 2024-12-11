package org.example.day11

import org.example.readAsString

fun main() {
//    val input = "day11/test_input".readAsString()
    val input = "day11/input".readAsString()

    var stones = input.split(" ").map { it.toLong() }.groupBy { it }.map { it.key to it.value.size.toLong() }

    for (i in 1..25) {
        stones = applyRules(stones)
    }

    println(stones.sumOf { it.second })


    // Part 2

    stones = input.split(" ").map { it.toLong() }.groupBy { it }.map { it.key to it.value.size.toLong() }

    for (i in 1..75) {
        stones = applyRules(stones)
    }
    println(stones.sumOf { it.second })
}

fun applyRules(stones: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
    val newList = stones.flatMap {
        if (it.first == 0L) {
            listOf(1L to (it.second))
        } else if (it.first.toString().length % 2 == 0) {
            val numberOfDigits = it.first.toString().length
            val leftStone = it.first.toString().substring(0, numberOfDigits / 2).toLong()
            val rightStone = it.first.toString().substring(numberOfDigits / 2, numberOfDigits).toLong()
            listOf(leftStone to (it.second) , rightStone to (it.second))
        } else {
            listOf((it.first * 2024) to (it.second))
        }
    }
    return newList.groupBy { it.first }.map { it.key to it.value.sumOf { a -> a.second } }
}
