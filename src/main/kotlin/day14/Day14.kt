package org.example.day14

import org.example.readAsString
import java.io.File
import kotlin.math.abs

data class Robot(val x: Int, val y: Int, val velocityX: Int, val velocityY: Int)

fun main() {
    val input = "day14/input".readAsString()
//    val input = "day14/test_input".readAsString()
//    val input = "day14/test_input2".readAsString()
//    val xWidth = 11
//    val yWidth = 7
    val xWidth = 101
    val yWidth = 103

    var robots = input.split(System.lineSeparator()).map { line ->
        "-?\\d+".toRegex().findAll(line).map { it.value.toInt() }.toList().let {
            Robot(it[0], it[1], it[2], it[3])
        }
    }

    val part1Robots = calculateRobots(100, robots, xWidth, yWidth)
    val quadrant1 = part1Robots.filter { it.x > xWidth/2 && it.y < yWidth/2 }
    val quadrant2 = part1Robots.filter { it.x < xWidth/2 && it.y < yWidth/2 }
    val quadrant3 = part1Robots.filter { it.x < xWidth/2 && it.y > yWidth/2 }
    val quadrant4 = part1Robots.filter { it.x > xWidth/2 && it.y > yWidth/2 }
    println("part1: ${quadrant1.size * quadrant2.size * quadrant3.size * quadrant4.size}")


    //part2
    // since there is no information how the christmas tree should look like I just assume there are at least 17 robots in line.
    // so I just build the resulting grid as a String and check if there it contains 17 # (# == Robot) in a row
    // It works, but takes some time so ¯\_(ツ)_/¯

    var part2Robots = robots
    var count = 0
    while (true) {
        var str = ""
        count++
        part2Robots = calculateRobots(part2Robots, xWidth, yWidth)

        for (x in 0 until xWidth) {
            for (y in 0 until yWidth) {
                str += if (part2Robots.any { it.x == x && it.y == y }) "#" else " "
            }
            str += System.lineSeparator()
        }
        if(str.contains("#################")){
            println("part2: $count")
            break
        }
    }
}

fun calculateRobots(times: Int, robots: List<Robot>, xWidth: Int, yWidth : Int): List<Robot> {
    return (1..times).fold(robots) { robots, _ ->
        calculateRobots(robots, xWidth, yWidth)
    }
}

private fun calculateRobots(
    robots: List<Robot>,
    xWidth: Int,
    yWidth: Int
) = robots.map { robot ->
    val newX = abs((robot.x + robot.velocityX + xWidth) % xWidth)
    val newY = abs((robot.y + robot.velocityY + yWidth) % yWidth)
    robot.copy(x = newX, y = newY)
}