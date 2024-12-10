package org.example.day10

import org.example.readAsString

fun main() {
//    val input = "day10/test_input".readAsString()
//    val input = "day10/test_input2".readAsString()
    val input = "day10/input".readAsString()

    val map = input.lines().map { line ->
        line.map {
            if (it == '.') { //only used for testing inputs with . instead of numbers for easier understanding
                null
            } else {
                it.toString().toInt()
            }
        }
    }
    val startPositions = map.withIndex().flatMap { (index, row) ->
        row.withIndex().filter {
            it.value == 0
        }.map {
            index to it.index
        }
    }


    val paths = startPositions.map { startPosition ->
        startPosition to calculatePaths(startPosition, map, listOf())
    }

    val result = paths.fold(0) { acc, path ->
        val trailStartEnd = path.second.filter { it.size == 10 }.map { it.first() to it.last() }
        val trailHeadScore = trailStartEnd.distinct().count()
        println(trailHeadScore)
        println(path)
        acc + trailHeadScore
    }

    println(result)
}

fun calculatePaths(
    currentPosition: Pair<Int, Int>,
    map: List<List<Int?>>,
    path: List<Pair<Int, Int>>
): List<List<Pair<Int, Int>>> {
    val bottom = map.getOrNull(currentPosition.first + 1)?.getOrNull(currentPosition.second) ?: -1
    val top = map.getOrNull(currentPosition.first - 1)?.getOrNull(currentPosition.second) ?: -1
    val left = map.getOrNull(currentPosition.first)?.getOrNull(currentPosition.second + 1) ?: -1
    val right = map.getOrNull(currentPosition.first)?.getOrNull(currentPosition.second - 1) ?: -1

    val newPositions = listOf(
        (currentPosition.first + 1 to currentPosition.second) to bottom,
        (currentPosition.first - 1 to currentPosition.second) to top,
        (currentPosition.first to currentPosition.second + 1) to left,
        (currentPosition.first to currentPosition.second - 1) to right,
    ).filter {
        it.second - (map[currentPosition.first][currentPosition.second] ?: 11) == 1
    } // 11 will only be used for non numerical positions which is only possible with test inputs containing '.'

    if (newPositions.isEmpty()) {
        return listOf(listOf(*path.toTypedArray(), currentPosition))
    }

    return newPositions.flatMap { newPosition ->
        calculatePaths(newPosition.first, map, listOf(*path.toTypedArray(), currentPosition))
    }
}