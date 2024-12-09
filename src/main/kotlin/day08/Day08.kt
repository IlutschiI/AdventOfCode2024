package org.example.day08

import org.example.readAsString

fun main() {
//    val input = "day08/test_input".readAsString()
//    val input = "day08/test_input2".readAsString()
//    val input = "day08/test_input3".readAsString()
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
            antinodes
        }
    }.distinct()

    println(result.count())

    // Part 2
    val result2 = groupedAntennas.flatMap { (_, antennas) ->
        antennas.flatMapIndexed { _, pair ->
            val differences = antennas.map {
                it.second.first - pair.second.first to it.second.second - pair.second.second
            }.filter { it != 0 to 0 }

            val antinodes = differences.flatMap {
                var newAntinodes= emptyList<Pair<Int,Int>>()
                var newAntinode = pair.second.first - it.first to pair.second.second - it.second

                var count = 1
                while (newAntinode.first in inputHeight && newAntinode.second in inputWidth) {
                    newAntinodes+= newAntinode
                    count++
                    newAntinode = pair.second.first - (it.first*count) to pair.second.second - (it.second*count)
                }

                newAntinode = pair.second.first + it.first to pair.second.second + it.second

                count = 1
                while (newAntinode.first in inputHeight && newAntinode.second in inputWidth) {
                    newAntinodes+= newAntinode
                    count++
                    newAntinode = pair.second.first + (it.first*count) to pair.second.second + (it.second*count)
                }

                newAntinodes
            }.filter {
                it.first in inputHeight && it.second in inputWidth
            }
            antinodes
        }
    }.distinct()

    println(result2.count())
}