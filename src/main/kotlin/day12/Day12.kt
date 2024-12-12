package org.example.day12

import org.example.readAsString

fun main() {
//    val input = "day12/test_input".readAsString()
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

    regions.map { region ->
        region.map { plant ->
            val top = region.find { it.first == plant.first - 1 && it.second == plant.second }
            val bottom = region.find { it.first == plant.first + 1 && it.second == plant.second }
            val left = region.find { it.first == plant.first && it.second == plant.second -1 }
            val right = region.find { it.first == plant.first && it.second == plant.second +1 }

            val surroundingPlants = listOfNotNull(top, bottom, left, right).size

            4-surroundingPlants
        }.sum() * region.size
    }.sum().run {
        println(this)
    }
}


fun findRegion(
    map: List<String>,
    currentPosition: Pair<Int, Int>,
    region: List<Pair<Int, Int>>,
    plant: Char
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