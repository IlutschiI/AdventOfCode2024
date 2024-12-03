package org.example.day01

import org.example.readAsString
import kotlin.math.absoluteValue

fun main() {
    val input = "day01/input".readAsString()
    val left = mutableListOf<Int>()
    val right = mutableListOf<Int>()
    input.split(System.lineSeparator()).forEach {
        val split=it.split(Regex("\\s+"))
        left += split[0].toInt()
        right += split[1].toInt()
    }
    left.sort()
    right.sort()

    val distance = left.foldIndexed(0) {index, acc, i -> acc+(i-right[index]).absoluteValue }

    val distancePart2 = left.fold(0){acc, i -> acc + i * right.filter { it==i }.size }
    println(distance)
    println(distancePart2)
}