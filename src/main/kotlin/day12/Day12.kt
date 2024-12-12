package org.example.day12

import org.example.readAsString

enum class Direction(val rowDiff: Int, val columnDiff: Int) {
    TOP(-1, 0), BOTTOM(1, 0), LEFT(0, -1), RIGHT(0, 1);
}

fun main() {
//    val input = "day12/test_input".readAsString()
//    val input = "day12/test_input2".readAsString()
    val input = "day12/input".readAsString()

    val map = input.lines()
    val regions = mutableListOf<List<Pair<Int, Int>>>()

    map.forEachIndexed { index, line ->
        line.forEachIndexed { k, char ->
            if (!regions.any { it.contains(index to k) }) {
                val r = findRegion(map, index to k, emptyList(), char)
                regions += r
            }
        }
    }

    regions.sumOf { region ->
        region.sumOf { plant ->
            val top = region.find { it.first == plant.first - 1 && it.second == plant.second }
            val bottom = region.find { it.first == plant.first + 1 && it.second == plant.second }
            val left = region.find { it.first == plant.first && it.second == plant.second - 1 }
            val right = region.find { it.first == plant.first && it.second == plant.second + 1 }

            val surroundingPlants = listOfNotNull(top, bottom, left, right).size

            4 - surroundingPlants
        } * region.size
    }.run {
        println(this)
    }


    // Part 2


    regions.map { region ->
//        println()
//        println(map[region.first().first][region.first().second])

        val topFences = findFences(region, Direction.TOP)
//        println("top: $topFences")
//        println()

        val bottomFences = findFences(region, Direction.BOTTOM)
//        println("bottom: $bottomFences")
//        println()

        val leftFences = findFences(region, Direction.LEFT)
//        println("left: $leftFences")
//        println()

        val rightFences = findFences(region, Direction.RIGHT)
//        println("right: $rightFences")
//        println()

        (topFences + bottomFences + leftFences + rightFences) * region.size
    }.sum().run {
        println(this)
    }
}

private fun findFences(region: List<Pair<Int, Int>>, direction: Direction) =
    region.groupBy {
        when(direction){
            Direction.TOP, Direction.BOTTOM -> it.first
            Direction.LEFT, Direction.RIGHT -> it.second
        }
    }.map { regionGroup ->
        var rowSplits = mutableListOf<List<Int>>()
        val pairs = regionGroup.value.map { plant ->
            val otherPlant = when (direction) {
                Direction.TOP -> region.find { it.first == plant.first - 1 && it.second == plant.second }
                Direction.BOTTOM -> region.find { it.first == plant.first + 1 && it.second == plant.second }
                Direction.LEFT -> region.find { it.first == plant.first && it.second == plant.second - 1 }
                Direction.RIGHT -> region.find { it.first == plant.first && it.second == plant.second + 1 }
            }
            if (otherPlant == null) {
                1 to plant
            } else {
                0 to plant
            }
        }

        var currentSplit = mutableListOf<Int>()
        val sortedPairs = when (direction) {
            Direction.TOP, Direction.BOTTOM -> pairs.sortedBy { it.second.second }
            Direction.LEFT, Direction.RIGHT -> pairs.sortedBy { it.second.first }
        }
        sortedPairs.forEachIndexed { index, pair ->
            val nextPair = sortedPairs.getOrElse(index + 1) { pair }
            currentSplit += pair.first
            val isSameSplit = when (direction) {
                Direction.TOP, Direction.BOTTOM -> pair.first == nextPair.first && Math.abs(pair.second.second - nextPair.second.second) <= 1 && pair.second.first == nextPair.second.first
                Direction.LEFT, Direction.RIGHT -> pair.first == nextPair.first && Math.abs(pair.second.first - nextPair.second.first) <= 1 && pair.second.second == nextPair.second.second
            }
            if (isSameSplit) {
            } else {
                rowSplits += currentSplit
                currentSplit = mutableListOf()
            }
        }.also {
            rowSplits += currentSplit
//            println(rowSplits)
        }
        rowSplits = rowSplits.filter { rowSplit -> rowSplit.all { it != 0 } }.toMutableList()
        rowSplits.size
    }.sum()


fun findRegion(
    map: List<String>, currentPosition: Pair<Int, Int>, region: List<Pair<Int, Int>>, plant: Char
): List<Pair<Int, Int>> {
    val top = (currentPosition.first - 1 to currentPosition.second) to map.getOrNull(currentPosition.first - 1)
        ?.getOrNull(currentPosition.second)
    val bottom = (currentPosition.first + 1 to currentPosition.second) to map.getOrNull(currentPosition.first + 1)
        ?.getOrNull(currentPosition.second)
    val left = (currentPosition.first to currentPosition.second - 1) to map.getOrNull(currentPosition.first)
        ?.getOrNull(currentPosition.second - 1)
    val right = (currentPosition.first to currentPosition.second + 1) to map.getOrNull(currentPosition.first)
        ?.getOrNull(currentPosition.second + 1)

    var newRegion = region + currentPosition

    listOf(top, bottom, left, right).filter { it.second != null && it.first !in region && it.second == plant }.forEach {
        newRegion = newRegion + findRegion(map, it.first, newRegion, plant)
    }

    return newRegion.distinct()
}