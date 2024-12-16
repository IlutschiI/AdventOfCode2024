package org.example.day15

import org.example.readAsString

enum class Direction(val xDiff: Int, val yDiff: Int) {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0)
}

fun main() {
    val input = "day15/input".readAsString()
//    val input = "day15/test_input".readAsString()
//    val input = "day15/test_input2".readAsString()
//    val input = "day15/test_input3".readAsString()

    val inputParts = input.split(System.lineSeparator() + System.lineSeparator())
    val map = inputParts[0].split(System.lineSeparator()).map { it.toCharArray().toList() }
    val inputs = inputParts[1].split(System.lineSeparator()).flatMap { it.toCharArray().toList() }.map {
        when (it) {
            '^' -> Direction.UP
            'v' -> Direction.DOWN
            '>' -> Direction.RIGHT
            '<' -> Direction.LEFT
            else -> error("Unknown input")
        }
    }
    val robotPosition = getRoboterPosition(map)

    var robotPosPart1 = robotPosition
    var mapPart1 = map.map { it.toMutableList() }.toMutableList()
    mapPart1[robotPosPart1.first][robotPosPart1.second] = '.'
    inputs.forEach { direction ->
        val newPos = robotPosPart1.first + direction.yDiff to robotPosPart1.second + direction.xDiff
        if (mapPart1[newPos.first][newPos.second] == '#') return@forEach
        if (mapPart1[newPos.first][newPos.second] == 'O') {
            val boxes = findAllBoxesInDirection(mapPart1, newPos, direction)
            val canMoveBoxes =
                mapPart1[boxes.last().first + direction.yDiff][boxes.last().second + direction.xDiff] == '.'
            if (canMoveBoxes) {
                mapPart1[boxes.first().first][boxes.first().second] = '.'
                mapPart1[boxes.last().first + direction.yDiff][boxes.last().second + direction.xDiff] = 'O'
            } else {
                return@forEach
            }
        }
        robotPosPart1 = newPos
    }

    var gps = 0
    mapPart1.forEachIndexed { rowIndex, chars ->
        chars.forEachIndexed { columnIndex, c ->
            if (c == 'O') {
                gps += (rowIndex * 100 + columnIndex)
            }
        }
    }

    println("part2: ${calculateGpsValue(mapPart1, 'O')}")



    //part2

    var mapPart2 = map.map {
        it.flatMap { c ->
            when (c) {
                '.' -> mutableListOf('.', '.')
                '#' -> mutableListOf('#', '#')
                'O' -> mutableListOf('[', ']')
                '@' -> mutableListOf('@', '.')
                else -> error("Unknown input")
            }
        }.toMutableList()
    }.toMutableList()
    var robotPosPart2 = getRoboterPosition(mapPart2)
    mapPart2[robotPosPart2.first][robotPosPart2.second] = '.'

    inputs.forEach { direction ->
        val newPos = robotPosPart2.first + direction.yDiff to robotPosPart2.second + direction.xDiff
        if (mapPart2[newPos.first][newPos.second] == '#') return@forEach
        if (mapPart2[newPos.first][newPos.second] == '[' || mapPart2[newPos.first][newPos.second] == ']') {
            val boxes = findAllBoxesInDirectionPart2(mapPart2, newPos, direction, listOf())
            var newMap = mapPart2.map { it.toMutableList() }.toMutableList()
            val canMoveBoxes = getCanMoveBoxesPart2(boxes, mapPart2, direction)
            if (canMoveBoxes) {
                boxes.forEach {
                    newMap[it.first.first][it.first.second] = '.'
                    newMap[it.second.first][it.second.second] = '.'
                }
                boxes.forEach {
                    val char1 = mapPart2[it.first.first][it.first.second]
                    val char2 = mapPart2[it.second.first][it.second.second]
                    newMap[it.first.first + direction.yDiff][it.first.second + direction.xDiff] = char1
                    newMap[it.second.first + direction.yDiff][it.second.second + direction.xDiff] = char2
                }
                mapPart2 = newMap
            } else {
                return@forEach
            }
        }
        robotPosPart2 = newPos
    }

    println("part2: ${calculateGpsValue(mapPart2, '[')}")

}

private fun calculateGpsValue(
    mapPart2: MutableList<MutableList<Char>>,
    boxIndicator: Char
): Int {
    var gps = 0
    mapPart2.forEachIndexed { rowIndex, chars ->
        chars.forEachIndexed { columnIndex, c ->
            if (c == boxIndicator) {
                gps += (rowIndex * 100 + columnIndex)
            }
        }
    }
    return gps
}

private fun getCanMoveBoxesPart2(
    boxes: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
    map: MutableList<MutableList<Char>>,
    direction: Direction
): Boolean {
    return when (direction) {
        Direction.LEFT, Direction.RIGHT -> {
            boxes.any {
                val box1NewPosChar = map[it.first.first + direction.yDiff][it.first.second + direction.xDiff]
                val box2NewPosChar = map[it.second.first + direction.yDiff][it.second.second + direction.xDiff]
                listOf(box1NewPosChar, box2NewPosChar).any { c -> c == '.' }
            }
        }

        Direction.UP, Direction.DOWN -> {
            boxes.all {
                val box1NewPosChar = map[it.first.first + direction.yDiff][it.first.second + direction.xDiff]
                val box2NewPosChar = map[it.second.first + direction.yDiff][it.second.second + direction.xDiff]
                listOf(box1NewPosChar, box2NewPosChar).none { c -> c == '#' }
            }
        }
    }
}

fun getRoboterPosition(map: List<List<Char>>): Pair<Int, Int> {
    map.forEachIndexed { rowIndex, chars ->
        chars.forEachIndexed { columnIndex, c ->
            if (c == '@') return@getRoboterPosition rowIndex to columnIndex
        }
    }
    error("Roboter not found")
}

fun findAllBoxesInDirection(
    map: List<List<Char>>,
    position: Pair<Int, Int>,
    direction: Direction
): List<Pair<Int, Int>> {
    var currentPosition = position
    var boxes = listOf<Pair<Int, Int>>()
    while (map[currentPosition.first][currentPosition.second] == 'O') {
        boxes += currentPosition
        currentPosition = currentPosition.first + direction.yDiff to currentPosition.second + direction.xDiff
    }
    return boxes
}

fun findAllBoxesInDirectionPart2(
    map: List<List<Char>>,
    position: Pair<Int, Int>,
    direction: Direction,
    boxes: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>
): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    if (direction == Direction.UP || direction == Direction.DOWN) {
        if (map[position.first][position.second] == '[') {
            return listOf(
                findAllBoxesInDirectionPart2(
                    map,
                    position.copy(first = position.first + direction.yDiff),
                    direction,
                    boxes + (position to position.copy(second = position.second + 1))
                ),
                findAllBoxesInDirectionPart2(
                    map,
                    position.copy(first = position.first + direction.yDiff, second = position.second + 1),
                    direction,
                    boxes
                )
            ).flatMap { it }.distinct()
        }
        if (map[position.first][position.second] == ']') {
            return listOf(
                findAllBoxesInDirectionPart2(
                    map,
                    position.copy(first = position.first + direction.yDiff),
                    direction,
                    boxes + (position to position.copy(second = position.second - 1))
                ),
                findAllBoxesInDirectionPart2(
                    map,
                    position.copy(first = position.first + direction.yDiff, second = position.second - 1),
                    direction,
                    boxes
                )
            ).flatMap { it }.distinct()
        }
        return boxes
    } else {
        var currentPosition = position
        var verticalBoxes = listOf<Pair<Int, Int>>()
        while (map[currentPosition.first][currentPosition.second] == '[' || map[currentPosition.first][currentPosition.second] == ']') {
            verticalBoxes += currentPosition
            currentPosition = currentPosition.first + direction.yDiff to currentPosition.second + direction.xDiff
        }
        return verticalBoxes.chunked(2).map {
            it[0] to it[1]
        }
    }
}