package org.example.day06

import org.example.readAsString

enum class Direction(val character: Char, val columnDirection: Int, val rowDirection: Int) {
    UP('^', -1, 0),
    RIGHT('>', 0, 1),
    DOWN('v', 1, 0),
    LEFT('<', 0, -1);

}

fun main() {
    val input = "day06/test_input".readAsString()
//    val input = "day06/input".readAsString()

    val split = input.split(System.lineSeparator())
    val rowsCount = split.size
    val columnsCount = split[0].length

    var direction = Direction.UP
    var pos = split.indexOfFirst {
        it.contains("^")
    }.let {
        it to split[it].indexOf(direction.character)
    }
    var visitedPositions = listOf<Pair<Int, Int>>()
    var isInbound = true

    while (isInbound) {
        visitedPositions += pos
        val nextPos = pos.first + direction.columnDirection to pos.second + direction.rowDirection
        isInbound = nextPos.first < rowsCount && nextPos.second < columnsCount
        if(isInbound){
            val nextChar = split[nextPos.first][nextPos.second]
            if (nextChar == '#'){
                direction = Direction.values()[(direction.ordinal + 1) % Direction.values().size]
            }else {
                pos = nextPos
            }
        }
    }

    println(visitedPositions.distinct().count())


    //Part 2 TODO
    var obstaclePositions = emptyList<Pair<Pair<Int, Int>, Direction>>()

    direction = Direction.UP
    pos = split.indexOfFirst {
        it.contains("^")
    }.let {
        it to split[it].indexOf(direction.character)
    }
    isInbound = true

    while (isInbound) {
        val nextPos = pos.first + direction.columnDirection to pos.second + direction.rowDirection
        isInbound = nextPos.first < rowsCount && nextPos.second < columnsCount
        if(isInbound){
            val nextChar = split[nextPos.first][nextPos.second]
            if (nextChar == '#'){
                obstaclePositions += nextPos to direction
                direction = Direction.values()[(direction.ordinal + 1) % Direction.values().size]
            }else {
                pos = nextPos
            }
        }
    }

    println(obstaclePositions)
}