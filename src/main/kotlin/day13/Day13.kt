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
        calculateGame(game) { it <= 100 }
    }.sum().let { println("Part1: $it") }

    games.mapNotNull { game ->
        calculateGame(
            game.copy(
                priceX = game.priceX + 10000000000000,
                priceY = game.priceY + 10000000000000
            )
        ) { true }
    }.sum().let { println("Part2: $it") }

}

fun calculateGame(game: Game, checkAmountOfPresses: (Long) -> Boolean): Long? {
    //when starting Pressing B
    val buttonBPresses =
        (game.buttonA.x * game.priceY - game.buttonA.y * game.priceX) / (game.buttonA.x * game.buttonB.y - game.buttonB.x * game.buttonA.y).toDouble()
    val buttonAPresses = (game.priceX - game.buttonB.x * buttonBPresses) / game.buttonA.x.toDouble()

    //when starting Pressing A
    val buttonBPresses2 =
        (game.buttonA.y * game.priceX - game.buttonA.x * game.priceY) / (game.buttonB.x * game.buttonA.y - game.buttonA.x * game.buttonB.y).toDouble()
    val buttonAPresses2 = (game.priceX - game.buttonB.x * buttonBPresses2) / game.buttonA.x.toDouble()

    var result1: Long? = null
    if (buttonAPresses.rem(1) == 0.0 &&
        buttonBPresses.rem(1) == 0.0 &&
        checkAmountOfPresses(buttonAPresses.toLong()) &&
        checkAmountOfPresses(buttonBPresses.toLong())
    ) {
        result1 = buttonAPresses.toLong() * 3 + buttonBPresses.toLong()
    }

    var result2: Long? = null
    if (buttonAPresses2.rem(1) == 0.0 &&
        buttonBPresses2.rem(1) == 0.0 &&
        checkAmountOfPresses(buttonAPresses2.toLong()) &&
        checkAmountOfPresses(buttonBPresses2.toLong())
    ) {
        result2 = buttonAPresses2.toLong() * 3 + buttonBPresses2.toLong()
    }

    return if (result1 != null || result2 != null) {
        Math.min(result1 ?: Long.MAX_VALUE, result2 ?: Long.MAX_VALUE)
    } else {
        null
    }
}
