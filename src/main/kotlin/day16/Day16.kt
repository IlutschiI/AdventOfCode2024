package org.example.day16

import org.example.readAsString
import java.util.stream.Collectors

enum class Direction(val xDiff: Int, val yDiff: Int) {
    UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

    fun next(): Direction {
        return when (this) {
            UP -> RIGHT
            DOWN -> LEFT
            LEFT -> UP
            RIGHT -> DOWN
        }
    }

    fun previous(): Direction {
        return when (this) {
            UP -> LEFT
            DOWN -> RIGHT
            LEFT -> DOWN
            RIGHT -> UP
        }
    }
}

fun main() {
//    val input = "day16/test_input".readAsString()
//    val input = "day16/test_input2".readAsString()
    val input = "day16/input".readAsString()

    val map = input.split(System.lineSeparator()).map { it.toCharArray().toList() }
    val startPosition = getStartPosition(map)

    val paths = findPaths(map, startPosition, Direction.RIGHT, emptyList(), listOf())

    paths.map { path ->
        path to path.sumOf { it.second }
    }.minBy { it.second }.let {
        println(it)
        println(it.first.size)
    }

//    println(paths)

}

fun getStartPosition(map: List<List<Char>>): Pair<Int, Int> {
    map.forEachIndexed { rowIndex, chars ->
        chars.forEachIndexed { columnIndex, c ->
            if (c == 'S') return@getStartPosition rowIndex to columnIndex
        }
    }
    error("Start not found")
}

//var maxCost = 179704
var maxCost = Int.MAX_VALUE

fun findPaths(
    map: List<List<Char>>,
    currentPosition: Pair<Int, Int>,
    direction: Direction,
    visited: List<Pair<Int, Int>> = listOf(),
    path: List<Pair<Pair<Int, Int>, Int>>
): List<List<Pair<Pair<Int, Int>, Int>>> {

    if (path.size > 500) {
//        print(".")
        return listOf()
    }

    if (path.sumOf { it.second } > maxCost) {
//        print("+")
        return listOf()
    }

    if (map.getOrNull(currentPosition.first)?.getOrNull(currentPosition.second) == 'E') {
        maxCost = Math.min(path.sumOf { it.second }, maxCost)
        println(path.sumOf { it.second })
        return listOf(path)
    }

    val straightPos = currentPosition.first + direction.yDiff to currentPosition.second + direction.xDiff
    val straightChar = map.getOrNull(straightPos.first)?.getOrNull(straightPos.second)
    val straight = straightChar == '.' || straightChar == 'E'

    val leftPos =
        currentPosition.first + direction.previous().yDiff to currentPosition.second + direction.previous().xDiff
    val leftCharacter = map.getOrNull(leftPos.first)?.getOrNull(leftPos.second)
    val left = leftCharacter == '.' || straightChar == 'E'

    val rightPos = currentPosition.first + direction.next().yDiff to currentPosition.second + direction.next().xDiff
    val rightChar = map.getOrNull(rightPos.first)?.getOrNull(rightPos.second)
    val right = rightChar == '.' || straightChar == 'E'

    var newPositions = listOf<Pair<Pair<Pair<Int, Int>, Int>, Direction>>()

    if (straight && straightPos !in visited) {
        newPositions += (straightPos to 1) to direction
    }
    if (left && leftPos !in visited) {
        newPositions += (leftPos to 1001) to direction.previous()
    }
    if (right && rightPos !in visited) {
        newPositions += (rightPos to 1001) to direction.next()
    }

    if (newPositions.isEmpty()) {
        return listOf()
    }

//    return newPositions.flatMap { newPosition ->
//    findPaths(
//        map,
//        newPosition.first.first, //new Position
//        newPosition.second, //Direction
//        visited + currentPosition,
//        path + newPosition.first
//    )
//    }

    return newPositions.parallelStream().flatMap { newPosition ->
        findPaths(
            map,
            newPosition.first.first, //new Position
            newPosition.second, //Direction
            visited + currentPosition,
            path + newPosition.first
        ).stream()
    }.collect(Collectors.toList())
}

//179704