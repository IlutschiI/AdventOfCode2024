package org.example.day08

import org.example.readAsString

fun main() {
//    val input = "day08/test_input".readAsString()
//    val input = "day08/test_input2".readAsString()
    val input = "day08/input".readAsString()

    val inputWidth = 0..input.lines().first().length-1
    val inputHeight = 0..input.lines().size-1

    val antennas = input.lines().flatMapIndexed { i, line ->
        line.mapIndexedNotNull { index, c ->
            if (c in '0'..'9' || c in 'a'..'z' || c in 'A'..'Z') {
                c to (i to index)
            } else null
        }
    }

    val groupedAntennas = antennas.groupBy { it.first }
    val result = groupedAntennas.flatMap { (_, antennas) ->
        antennas.flatMapIndexed { _, pair ->
            val differences = antennas.map {
                it.second.first - pair.second.first to it.second.second - pair.second.second
            }.filter { it != 0 to 0 }

            val antinodes = differences.map { pair.second.first - it.first to pair.second.second - it.second }.filter {
                it.first in inputHeight && it.second in inputWidth
            }
//            println(pair)
//            println(differences)
//            println(antinodes)
//            println()
            antinodes
        }
    }.distinct()

    println(result.count())
}