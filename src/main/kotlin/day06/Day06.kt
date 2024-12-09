package org.example.day06

import org.example.readAsString
import java.util.stream.Collectors

enum class Direction(val character: Char, val columnDirection: Int, val rowDirection: Int) {
    UP('^', -1, 0),
    RIGHT('>', 0, 1),
    DOWN('v', 1, 0),
    LEFT('<', 0, -1);

    fun next(): Direction {
        return Direction.entries.toTypedArray()[(this.ordinal + 1) % Direction.entries.size]
    }
}

fun main() {
//    val input = "day06/test_input".readAsString()
    val input = "day06/input".readAsString()

    val split = input.split(System.lineSeparator())
    val rowsCount = split.size
    val columnsCount = split[0].length

    var direction = Direction.UP
    var pos = split.indexOfFirst {
        it.contains("^")
    }.let {
        it to split[it].indexOf(direction.character)
    }
    val visitedPositions = mutableListOf<Pair<Pair<Int, Int>, Direction>>()
    val obstaclePositions = mutableListOf<Pair<Pair<Int, Int>, Direction>>()
    var isInbound = true

    while (isInbound) {
        visitedPositions += pos to direction
        val nextPos = pos.first + direction.columnDirection to pos.second + direction.rowDirection
        isInbound = nextPos.first < rowsCount && nextPos.second < columnsCount
        if (isInbound) {
            val nextChar = split[nextPos.first][nextPos.second]
            if (nextChar == '#') {
                obstaclePositions += nextPos to direction
                direction = direction.next()
            } else {
                pos = nextPos
            }
        }
    }

    println(visitedPositions.map { it.first }.distinct().count())


    //Part 2
    val newObstaclePositions = visitedPositions.distinctBy { it.first }.drop(1).parallelStream().map {
        var direction = Direction.UP
        var pos = split.indexOfFirst {
            it.contains("^")
        }.let {
            it to split[it].indexOf(direction.character)
        }
        if (it.first == pos) {
            return@map null
        }
        val visitedPositions2 = mutableListOf<Pair<Pair<Int, Int>, Direction>>()
        var isInbound = true
        val newSplit = split.toMutableList()
        newSplit[it.first.first] = newSplit[it.first.first].replaceRange(it.first.second, it.first.second + 1, "#")

        while (isInbound) {
            val currentPos = pos to direction
            if (currentPos in visitedPositions2) {
                return@map it.first
            }
            visitedPositions2 += currentPos
            val nextPos = pos.first + direction.columnDirection to pos.second + direction.rowDirection
            isInbound =
                nextPos.first < rowsCount && nextPos.first != -1 && nextPos.second < columnsCount && nextPos.second != -1
            if (isInbound) {
                val nextChar = newSplit[nextPos.first][nextPos.second]
                if (nextChar == '#') {
                    obstaclePositions += nextPos to direction
                    direction = direction.next()
                } else {
                    pos = nextPos
                }
            }
        }
        return@map null

    }.collect(Collectors.toList()).filterNotNull()

    println("possibel obstacle positions: ${newObstaclePositions.distinct().count()}")

}

fun findObstacleInDirection(grid: List<String>, startPos: Pair<Int, Int>, direction: Direction): Pair<Int, Int>? {
    var isInbound = true
    var pos = startPos
    while (isInbound) {
        val nextPos = pos.first + direction.columnDirection to pos.second + direction.rowDirection
        isInbound = grid.getOrNull(nextPos.first)?.getOrNull(nextPos.second) != null
        if (isInbound) {
            val nextChar = grid[nextPos.first][nextPos.second]
            if (nextChar == '#') {
                return nextPos
            } else {
                pos = nextPos
            }
        }
    }
    return null
}