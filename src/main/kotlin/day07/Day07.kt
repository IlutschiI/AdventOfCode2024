package org.example.day07

import org.example.readAsString

enum class Operations {
    PLUS, MULTIPLY, DIVIDE, MINUS
}

fun main() {
//    val input = "day07/test_input".readAsString()
    val input = "day07/input".readAsString()

    val operations = listOf(Operations.PLUS, Operations.MULTIPLY)

    val lines = input.lines()
    lines.fold(0L) { acc, it ->
        val (result, numbers) = it.split(": ").let { res ->
            res[0].toLong() to res[1].split(" ").map(String::toLong)
        }

        val operands = product(operations, numbers.size - 1)

        operands.filter {
            val calculated = calculate(numbers, it)
            calculated == result
        }.let {
            if (it.isNotEmpty()) {
                acc + result
            } else {
                acc
            }
        }
    }.let { println(it) }
}

fun calculate(numbers: List<Long>, operations: List<Operations>): Long {
    return numbers.reduceIndexed { index, acc, i ->
        when (operations[index - 1]) {
            Operations.PLUS -> acc + i
            Operations.MULTIPLY -> acc * i
            else -> acc
        }
    }
}

fun <T> product(values: List<T>, size: Int): List<List<T>> {
    if (size == 1) return values.map { listOf(it) }
    return values.flatMap { value ->
        product(values, size - 1).map { listOf(value) + it }
    }
}