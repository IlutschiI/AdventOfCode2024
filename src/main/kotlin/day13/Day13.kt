package org.example.day13

import org.example.readAsString

data class Game(
    val priceX: Int,
    val priceY: Int,
    val buttonA: Button,
    val buttonB: Button
)

data class Button(
    val x: Int,
    val y: Int
)

fun main() {
    val input = "day13/input".readAsString()
//    val input = "day13/test_input".readAsString()

    val games = input.split(System.lineSeparator() + System.lineSeparator()).map { line ->
        val split = line.split(System.lineSeparator())
        val buttonA =
            "\\d+".toRegex().findAll(split[0]).let { Button(it.first().value.toInt(), it.last().value.toInt()) }
        val buttonB =
            "\\d+".toRegex().findAll(split[1]).let { Button(it.first().value.toInt(), it.last().value.toInt()) }
        val game = "\\d+".toRegex().findAll(split[2])
            .let { Game(it.first().value.toInt(), it.last().value.toInt(), buttonA, buttonB) }
        game
    }

    games.mapNotNull { game ->
        calculateGame(game).also { println(it) }
    }.sum().let { println(it) }

}

fun calculateGame(game: Game): Int? {
    var buttonA: Int? = null
    var buttonB: Int? = null
    var buttonA2: Int? = null
    var buttonB2: Int? = null

    for (i in Math.max(game.priceX / game.buttonA.x, 100) downTo 1) {
        val currentY = i * game.buttonA.y
        val currentX = i * game.buttonA.x
        val yDiffToPrice = game.priceY - currentY
        val xDiffToPrice = game.priceX - currentX


        if (xDiffToPrice >= 0 && xDiffToPrice % game.buttonB.x == 0 && yDiffToPrice >= 0 && yDiffToPrice % game.buttonB.y == 0) {
            val buttonBPresses = xDiffToPrice / game.buttonB.x
            if (currentY+buttonBPresses*game.buttonB.y == game.priceY && currentX+buttonBPresses*game.buttonB.x == game.priceX) {
                if (i <= 100 && xDiffToPrice / game.buttonB.x <= 100) {
                    buttonA = i
                    buttonB = xDiffToPrice / game.buttonB.x
                    break
                }
            }
        }
    }

    for (i in Math.max(game.priceX / game.buttonB.x, 100) downTo 1) {
        val currentY = i * game.buttonB.y
        val currentX = i * game.buttonB.x
        val yDiffToPrice = game.priceY - currentY
        val xDiffToPrice = game.priceX - currentX

        if (xDiffToPrice >= 0 && xDiffToPrice % game.buttonA.x == 0 && yDiffToPrice >= 0 && yDiffToPrice % game.buttonA.y == 0) {
            val buttonAPresses = xDiffToPrice / game.buttonA.x
            if (currentY+buttonAPresses*game.buttonA.y == game.priceY && currentX+buttonAPresses*game.buttonA.x == game.priceX) {
                if (i <= 100 && xDiffToPrice / game.buttonA.x <= 100) {
                    buttonB2 = i
                    buttonA2 = xDiffToPrice / game.buttonA.x
                    break
                }
            }

        }
    }

    var result1:Int? = null
    if (buttonA != null && buttonB != null) {
        result1 = buttonA * 3 + buttonB
    }

    var result2:Int? = null
    if (buttonA2 != null && buttonB2 != null) {
        result2 = buttonA2 * 3 + buttonB2
    }

    return if (result1 != null || result2 != null) {
        Math.min(result1?:Int.MAX_VALUE, result2?:Int.MAX_VALUE)
    } else {
        null
    }
}