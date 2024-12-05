package org.example.day04

import org.example.readAsString

fun main() {
//    val input = "day04/test_input".readAsString()
    val input = "day04/input".readAsString()
    var count = 0
    count += findXMAS(input, 1, 1)
    count += findXMAS(input, -1, -1)
    count += findXMAS(input, -1, 1)
    count += findXMAS(input, 1, -1)
    count += findXMAS(input, 0, 1)
    count += findXMAS(input, 0, -1)
    count += findXMAS(input, 1, 0)
    count += findXMAS(input, -1, 0)
    println(count)

    count = findXMASPart2(input)
    println(count)
}

fun findXMAS(input: String, lineDiff: Int, columnDiff: Int): Int {
    val split = input.split(System.lineSeparator())
    var count = 0
    split.forEachIndexed { index, line ->
        line.forEachIndexed { charIndex, char ->
            if (char == 'X'
                && split.getOrNull(index + lineDiff)?.getOrNull(charIndex + columnDiff) == 'M'
                && split.getOrNull(index + lineDiff * 2)?.getOrNull(charIndex + columnDiff * 2) == 'A'
                && split.getOrNull(index + lineDiff * 3)?.getOrNull(charIndex + columnDiff * 3) == 'S'
            ) {
                count++
            }
        }
    }
    return count
}

fun findXMASPart2(input: String): Int {
    val split = input.split(System.lineSeparator())
    var count = 0
    split.forEachIndexed { index, line ->
        line.forEachIndexed { charIndex, char ->

            if (char == 'A') {
                val leftRight = "" +
                        split.getOrNull(index - 1)?.getOrNull(charIndex - 1) +
                        split.getOrNull(index + 1)?.getOrNull(charIndex + 1)
                val rightLeft = "" +
                        split.getOrNull(index - 1)?.getOrNull(charIndex + 1) +
                        split.getOrNull(index + 1)?.getOrNull(charIndex - 1)

                val regex = "(MS|SM)".toRegex()
                if (leftRight.contains(regex)&&rightLeft.contains(regex)) {
                    count++
                }
            }
        }
    }
    return count
}