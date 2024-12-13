package org.example.day13

import org.example.readAsString

data class Game(
    val priceX: Long,
    val priceY: Long,
    val buttonA: Button,
    val buttonB: Button
)

data class Button(
    val x: Long,
    val y: Long
)

fun main() {
    val input = "day13/input".readAsString()
//    val input = "day13/test_input".readAsString()

    val games = input.split(System.lineSeparator() + System.lineSeparator()).map { line ->
        val split = line.split(System.lineSeparator())
        val buttonA =
            "\\d+".toRegex().findAll(split[0]).let { Button(it.first().value.toLong(), it.last().value.toLong()) }
        val buttonB =
            "\\d+".toRegex().findAll(split[1]).let { Button(it.first().value.toLong(), it.last().value.toLong()) }
        val game = "\\d+".toRegex().findAll(split[2])
            .let { Game(it.first().value.toLong(), it.last().value.toLong(), buttonA, buttonB) }
        game
    }

    games.mapNotNull { game ->
        calculateGame(game)
    }.sum().let { println(it) }

    games.mapNotNull { game ->
        calculateGamePart2(
            game.copy(
                priceX = game.priceX + 10000000000000,
                priceY = game.priceY + 10000000000000
            )
        )
    }.sum().let { println(it) }

}

fun calculateGame(game: Game): Long? {
    val buttonBPresses =
        (game.buttonA.x * game.priceY - game.buttonA.y * game.priceX) / (game.buttonA.x * game.buttonB.y - game.buttonB.x * game.buttonA.y).toDouble()
    val buttonAPresses = (game.priceX - game.buttonB.x*buttonBPresses) / game.buttonA.x.toDouble()

    val buttonBPresses2 =
        (game.buttonA.y * game.priceX - game.buttonA.x * game.priceY) / (game.buttonB.x * game.buttonA.y - game.buttonA.x * game.buttonB.y).toDouble()
    val buttonAPresses2 = (game.priceX - game.buttonB.x*buttonBPresses2) / game.buttonA.x.toDouble()

    var result1: Long? = null
    if (buttonAPresses.rem(1) == 0.0 && buttonBPresses.rem(1) == 0.0 && buttonAPresses <= 100 && buttonBPresses <= 100) {
        result1 = buttonAPresses.toLong()*3 + buttonBPresses.toLong()
    }

    var result2: Long? = null
    if (buttonAPresses2.rem(1) == 0.0 && buttonBPresses2.rem(1) == 0.0 && buttonAPresses2 <= 100 && buttonBPresses2 <= 100) {
        result2 = buttonAPresses2.toLong()*3 + buttonBPresses2.toLong()
    }

    return if (result1 != null || result2 != null) {
        Math.min(result1 ?: Long.MAX_VALUE, result2 ?: Long.MAX_VALUE)
    } else {
        null
    }
}

fun calculateGamePart2(game: Game): Long? {
    val buttonBPresses =
        (game.buttonA.x * game.priceY - game.buttonA.y * game.priceX) / (game.buttonA.x * game.buttonB.y - game.buttonB.x * game.buttonA.y).toDouble()
    val buttonAPresses = (game.priceX - game.buttonB.x*buttonBPresses) / game.buttonA.x.toDouble()

    val buttonBPresses2 =
        (game.buttonA.y * game.priceX - game.buttonA.x * game.priceY) / (game.buttonB.x * game.buttonA.y - game.buttonA.x * game.buttonB.y).toDouble()
    val buttonAPresses2 = (game.priceX - game.buttonB.x*buttonBPresses2) / game.buttonA.x.toDouble()

    var result1: Long? = null
    if (buttonAPresses.rem(1) == 0.0 && buttonBPresses.rem(1) == 0.0) {
        result1 = buttonAPresses.toLong()*3 + buttonBPresses.toLong()
    }

    var result2: Long? = null
    if (buttonAPresses2.rem(1) == 0.0 && buttonBPresses2.rem(1) == 0.0) {
        result2 = buttonAPresses2.toLong()*3 + buttonBPresses2.toLong()
    }

    return if (result1 != null || result2 != null) {
       Math.min(result1 ?: Long.MAX_VALUE, result2 ?: Long.MAX_VALUE)
    } else {
        null
    }
}