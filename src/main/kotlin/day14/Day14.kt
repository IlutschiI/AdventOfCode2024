package org.example.day14

import org.example.readAsString
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

    robots = calculateRobots(100, robots, xWidth, yWidth)
    val quadrant1 = robots.filter { it.x > xWidth/2 && it.y < yWidth/2 }
    val quadrant2 = robots.filter { it.x < xWidth/2 && it.y < yWidth/2 }
    val quadrant3 = robots.filter { it.x < xWidth/2 && it.y > yWidth/2 }
    val quadrant4 = robots.filter { it.x > xWidth/2 && it.y > yWidth/2 }
    println("part1: ${quadrant1.size * quadrant2.size * quadrant3.size * quadrant4.size}")
}

fun calculateRobots(times: Int, robots: List<Robot>, xWidth: Int, yWidth : Int): List<Robot> {
    return (1..times).fold(robots) { acc, _ ->
        acc.map { robot ->
            val newX = abs((robot.x + robot.velocityX + xWidth) % xWidth)
            val newY = abs((robot.y + robot.velocityY + yWidth) % yWidth)
            robot.copy(x = newX, y = newY)
        }
    }
}