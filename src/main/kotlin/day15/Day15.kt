package org.example.day15

import org.example.readAsString

enum class Direction(val xDiff: Int, val yDiff: Int) {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0)
}

fun main() {
    val input = "day15/input".readAsString()
//    val input = "day15/test_input".readAsString()
//    val input = "day15/test_input2".readAsString()

    val inputParts = input.split(System.lineSeparator() + System.lineSeparator())
    val map = inputParts[0].split(System.lineSeparator()).map { it.toCharArray().toList() }
    val inputs = inputParts[1].split(System.lineSeparator()).flatMap { it.toCharArray().toList() }.map {
        when (it) {
        '^' -> Direction.UP
        'v' -> Direction.DOWN
        '>' -> Direction.RIGHT
        '<' -> Direction.LEFT
        else -> error("Unknown input")
    } }
    val robotPosition = getRoboterPosition(map)

    var robotPosPart1 = robotPosition
    var mapPart1 = map.map { it.toMutableList() }.toMutableList()
    mapPart1[robotPosPart1.first][robotPosPart1.second] = '.'
    inputs.forEach { direction ->
        val newPos = robotPosPart1.first + direction.yDiff to robotPosPart1.second + direction.xDiff
        if(mapPart1[newPos.first][newPos.second] == '#') return@forEach
        if(mapPart1[newPos.first][newPos.second] == 'O') {
            val boxes = findAllBoxesInDirection(mapPart1, newPos, direction)
            val canMoveBoxes = mapPart1[boxes.last().first+direction.yDiff][boxes.last().second+direction.xDiff] == '.'
            if(canMoveBoxes){
                mapPart1[boxes.first().first][boxes.first().second] = '.'
                mapPart1[boxes.last().first+direction.yDiff][boxes.last().second+direction.xDiff] = 'O'
            } else {
                return@forEach
            }
        }
        robotPosPart1 = newPos
    }

    var gps = 0
    mapPart1.forEachIndexed { rowIndex, chars ->
        chars.forEachIndexed { columnIndex, c ->
            if (c == 'O'){
               gps += (rowIndex * 100 + columnIndex)
            }
        }
    }

    println(gps)
}

fun getRoboterPosition(map: List<List<Char>>): Pair<Int, Int> {
    map.forEachIndexed { rowIndex, chars ->
        chars.forEachIndexed { columnIndex, c ->
            if (c == '@') return@getRoboterPosition rowIndex to columnIndex
        }
    }
    error("Roboter not found")
}

fun findAllBoxesInDirection(map: List<List<Char>>, position: Pair<Int,Int>, direction: Direction): List<Pair<Int, Int>> {
    var currentPosition = position
    var boxes = listOf<Pair<Int, Int>>()
    while(map[currentPosition.first][currentPosition.second] == 'O') {
        boxes+= currentPosition
        currentPosition = currentPosition.first + direction.yDiff to currentPosition.second + direction.xDiff
    }
    return boxes
}