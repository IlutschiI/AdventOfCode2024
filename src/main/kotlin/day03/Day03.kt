package org.example.day03

import org.example.readAsString

fun main() {
    val input = "day03/input".readAsString()
    val result = findAndMultiply(input)
    println(result)


    val dontIndexes = """don't\(\)""".toRegex().findAll(input).map {
        it.range.first
    }.toMutableList().apply { addLast(input.length-1) }

    val doIndexes = """do\(\)""".toRegex().findAll(input).map {
        it.range.last
    }.toMutableList().apply { addFirst(0) }

    val doRanges = dontIndexes.mapIndexed { index, i ->
        val doIndex = doIndexes.first { it >= dontIndexes.getOrElse(index - 1) { 0 } }
        if (doIndex < i) doIndex to i else null
    }.filterNotNull()

    val sanitizedInput = doRanges.joinToString {
        input.substring(it.first, it.second)
    }

    val resultPart2 = findAndMultiply(sanitizedInput)
    println(resultPart2)
}

private fun findAndMultiply(sanitizedInput: String): Int {
    val multiplicationsPart2 = Regex("mul\\(\\d+,\\d+\\)").findAll(sanitizedInput).map {
        val values = Regex("\\d+").findAll(it.value).toList()
        values[0].value.toInt() to values[1].value.toInt()
    }
    val resultPart2 = multiplicationsPart2.fold(0) { acc, mul -> acc + mul.first * mul.second }
    return resultPart2
}