package org.example.day12

import org.example.readAsString

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

    regions.map { region ->
        region.map { plant ->
            val top = region.find { it.first == plant.first - 1 && it.second == plant.second }
            val bottom = region.find { it.first == plant.first + 1 && it.second == plant.second }
            val left = region.find { it.first == plant.first && it.second == plant.second - 1 }
            val right = region.find { it.first == plant.first && it.second == plant.second + 1 }

            val surroundingPlants = listOfNotNull(top, bottom, left, right).size

            4 - surroundingPlants
        }.sum() * region.size
    }.sum().run {
        println(this)
    }


    // Part 2

    regions.map { region ->
        println()
        println(map[region.first().first][region.first().second])

        val a = region.groupBy { it.first }.map {
            var rowSplits = mutableListOf<List<Int>>()
            val pairs = it.value.map { plant ->
                val top = region.find { it.first == plant.first - 1 && it.second == plant.second }
//                val bottom = region.find { it.first == plant.first + 1 && it.second == plant.second }
//                val left = region.find { it.first == plant.first && it.second == plant.second -1 }
//                val right = region.find { it.first == plant.first && it.second == plant.second +1 }

                val surroundingPlants = listOfNotNull(top, /*bottom left, right*/).size

                1 - surroundingPlants to plant
            }
            var currentSplit = mutableListOf<Int>()
            val sortedPairs = pairs.sortedBy { it.second.second }
            sortedPairs.forEachIndexed { index, pair ->
                val nextPair = sortedPairs.getOrElse(index + 1, { pair })
                currentSplit += pair.first
                if (pair.first == nextPair.first && pair.second.first == nextPair.second.first && Math.abs(pair.second.second - nextPair.second.second) <= 1) {
                } else {
                    rowSplits += currentSplit
                    currentSplit = mutableListOf()
                }
            }.also {
                rowSplits += currentSplit
                println(rowSplits)
            }
            rowSplits=rowSplits.filter { it.all { it!=0 } }.toMutableList()
            rowSplits.size
        }.sum()

        println("top: $a")
        println()



        val b = region.groupBy { it.first }.map {
            var rowSplits = mutableListOf<List<Int>>()
            val pairs = it.value.map { plant ->
//                val top = region.find { it.first == plant.first - 1 && it.second == plant.second }
                val bottom = region.find { it.first == plant.first + 1 && it.second == plant.second }
//                val left = region.find { it.first == plant.first && it.second == plant.second -1 }
//                val right = region.find { it.first == plant.first && it.second == plant.second +1 }

                val surroundingPlants = listOfNotNull(bottom, /*bottom left, right*/).size

                1 - surroundingPlants to plant
            }
            var currentSplit = mutableListOf<Int>()
            val sortedPairs = pairs.sortedBy { it.second.second }
            sortedPairs.forEachIndexed { index, pair ->
                val nextPair = sortedPairs.getOrElse(index + 1, { pair })
                currentSplit += pair.first
                if (pair.first == nextPair.first && pair.second.first == nextPair.second.first && Math.abs(pair.second.second - nextPair.second.second) <= 1) {
                } else {
                    rowSplits += currentSplit
                    currentSplit = mutableListOf()
                }
            }.also {
                rowSplits += currentSplit
                println(rowSplits)
            }
            rowSplits=rowSplits.filter { it.all { it!=0 } }.toMutableList()
            rowSplits.size
        }.sum()

        println("bottom: $b")
        println()



        val c = region.groupBy { it.second }.map {
            var rowSplits = mutableListOf<List<Int>>()
            val pairs = it.value.map { plant ->
//                val top = region.find { it.first == plant.first - 1 && it.second == plant.second }
//                val bottom = region.find { it.first == plant.first + 1 && it.second == plant.second }
                val left = region.find { it.first == plant.first && it.second == plant.second -1 }
//                val right = region.find { it.first == plant.first && it.second == plant.second +1 }

                val surroundingPlants = listOfNotNull(left, /*bottom left, right*/).size

                1 - surroundingPlants to plant
            }
            var currentSplit = mutableListOf<Int>()
            val sortedPairs = pairs.sortedBy { it.second.first }
            sortedPairs.forEachIndexed { index, pair ->
                val nextPair = sortedPairs.getOrElse(index + 1, { pair })
                currentSplit += pair.first
                if (pair.first == nextPair.first && Math.abs(pair.second.first - nextPair.second.first) <= 1 && pair.second.second == nextPair.second.second) {
                } else {
                    rowSplits += currentSplit
                    currentSplit = mutableListOf()
                }
            }.also {
                rowSplits += currentSplit
                println(rowSplits)
            }
            rowSplits=rowSplits.filter { it.all { it!=0 } }.toMutableList()
            rowSplits.size
        }.sum()

        println("left: $c")
        println()



        val d = region.groupBy { it.second }.map {
            var rowSplits = mutableListOf<List<Int>>()
            val pairs = it.value.map { plant ->
//                val top = region.find { it.first == plant.first - 1 && it.second == plant.second }
//                val bottom = region.find { it.first == plant.first + 1 && it.second == plant.second }
//                val left = region.find { it.first == plant.first && it.second == plant.second -1 }
                val right = region.find { it.first == plant.first && it.second == plant.second +1 }

                val surroundingPlants = listOfNotNull(right, /*bottom left, right*/).size

                1 - surroundingPlants to plant
            }
            var currentSplit = mutableListOf<Int>()
            val sortedPairs = pairs.sortedBy { it.second.first }
            sortedPairs.forEachIndexed { index, pair ->
                val nextPair = sortedPairs.getOrElse(index + 1, { pair })
                currentSplit += pair.first
                if (pair.first == nextPair.first && Math.abs(pair.second.first - nextPair.second.first) <= 1 && pair.second.second == nextPair.second.second) {
                } else {
                    rowSplits += currentSplit
                    currentSplit = mutableListOf()
                }
            }.also {
                rowSplits += currentSplit
                println(rowSplits)
            }
            rowSplits=rowSplits.filter { it.all { it!=0 } }.toMutableList()
            rowSplits.size
        }.sum()

        println("right: $d")
        println()

        (a+b+c+d)*region.size

//        val b = region.groupBy { it.second }.map {
//            it.value.map { plant ->
////                val top = region.find { it.first == plant.first - 1 && it.second == plant.second }
////                val bottom = region.find { it.first == plant.first + 1 && it.second == plant.second }
//                val left = region.find { it.first == plant.first && it.second == plant.second - 1 }
//                val right = region.find { it.first == plant.first && it.second == plant.second + 1 }
//
//                val surroundingPlants = listOfNotNull(/*top, bottom,*/ left, right).size
//
//                2 - surroundingPlants
//            }.groupBy { it }.map { it.key * it.value.size }.sum()
//        }.run {
//            println(this)
//            this
//        }.sum()
//        println(a + b)
//        a + b
//        region.map { plant ->
//            val top = region.find { it.first == plant.first - 1 && it.second == plant.second }
//            val bottom = region.find { it.first == plant.first + 1 && it.second == plant.second }
//            val left = region.find { it.first == plant.first && it.second == plant.second -1 }
//            val right = region.find { it.first == plant.first && it.second == plant.second +1 }
//
//            val surroundingPlants = listOfNotNull(top, bottom, left, right).size
//
//            4-surroundingPlants
//        }
    }.sum().run {
        println(this)
    }
}


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