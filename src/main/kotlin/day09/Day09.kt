package org.example.day09

import org.example.readAsString

fun main() {
//    val input = "day09/test_input".readAsString()
    val input = "day09/input".readAsString()

    val diskMap = input.flatMapIndexed { index, c ->

        val fillCharacter = if (index % 2 == 0) "${index / 2}" else "."

        val value = c.toString().toInt()
        List(value) { fillCharacter }
    }

//    println(diskMap)

    val defragmentedDiskMap = diskMap.toMutableList()
    defragmentedDiskMap.forEachIndexed { index, s ->
        if (s == "."){
            for (i in defragmentedDiskMap.indices.reversed()){
                if (i<index){
                    break
                }
                if (defragmentedDiskMap[i].matches("\\d+".toRegex())){
                    defragmentedDiskMap[index]=defragmentedDiskMap[i]
                    defragmentedDiskMap[i]="."
                    break
                }
            }
        }
    }

//    println(defragmentedDiskMap.joinToString(""))

    val checksum = defragmentedDiskMap.mapIndexed { index, s ->
        if (s.matches("\\d+".toRegex())){
            index * s.toLong()
        } else {
            0
        }
    }.sum()

    println(checksum)

}