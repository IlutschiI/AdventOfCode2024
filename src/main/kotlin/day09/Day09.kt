package org.example.day09

import org.example.readAsString
import java.lang.StringBuilder
import java.math.BigInteger

fun main() {
//    val input = "day09/test_input3".readAsString()
    val input = "day09/input".readAsString()

    val diskMap = input.flatMapIndexed { index, c ->

        val fillCharacter = if (index % 2 == 0) index / 2 else null

        val value = c.toString().toInt()
        List(value) { fillCharacter }
    }

    val defragmentedDiskMap = diskMap.map { it?.toString()?:"." }.toMutableList()
    defragmentedDiskMap.forEachIndexed { index, s ->
        if (s == ".") {
            for (i in defragmentedDiskMap.indices.reversed()) {
                if (i < index) {
                    break
                }
                if (defragmentedDiskMap[i].matches("\\d+".toRegex())) {
                    defragmentedDiskMap[index] = defragmentedDiskMap[i]
                    defragmentedDiskMap[i] = "."
                    break
                }
            }
        }
    }

    val checksum = calculateChecksum(defragmentedDiskMap)
    println(checksum)


    //part2
    var fragmentedDiskMap2 = diskMap.toList()
    var defragmentedDiskMap2 = diskMap.map { if(it!=null)it.toString() else "." }.toMutableList()

    val ids = diskMap.distinct().filterNotNull().sortedDescending()
    var searchLimit = defragmentedDiskMap2.size
    ids.forEach { id ->
        val searchString = fragmentedDiskMap2.subList(0, searchLimit).dropLastWhile { it == null }
        val startIndex = searchString.indexOf(id)
        val endIndex = searchString.lastIndexOf(id)

        val requiredSpace = (1+endIndex - startIndex)
        val searchString2 = defragmentedDiskMap2.subList(0, startIndex)

        val indexOfFreeSpace = searchString2.mapIndexed { index, i ->
            if (i == "." && searchString2.size >= index + requiredSpace) {
                searchString2.subList(index, index + requiredSpace).all { it == "." }
            } else {
                false
            }
        }.withIndex().firstOrNull { it.value }?.index
        if (indexOfFreeSpace != null) {

            for (i in startIndex..endIndex) {
                defragmentedDiskMap2[i]="."
            }

            for (i in indexOfFreeSpace until (indexOfFreeSpace + requiredSpace)) {
                defragmentedDiskMap2[i]=id.toString()
            }
        }

        searchLimit = startIndex
    }

    println(calculateChecksum(defragmentedDiskMap2))
}

private fun calculateChecksum(defragmentedDiskMap: List<String>) =
    defragmentedDiskMap.mapIndexed { index, s ->
        if (s.matches("\\d+".toRegex())) {
            index.toBigInteger() * s.toBigInteger()
        } else {
            BigInteger.ZERO
        }
    }.sumOf { it }
