package org.example.day16test

import java.util.PriorityQueue

data class Node(val x: Int, val y: Int, val g: Int, val h: Int, val parent: Node? = null) : Comparable<Node> {
    val f: Int get() = g + h
    override fun compareTo(other: Node) = this.f.compareTo(other.f)
}

fun heuristic(x1: Int, y1: Int, x2: Int, y2: Int): Int {
    // Manhattan-Distanz
    return Math.abs(x1 - x2) + Math.abs(y1 - y2)
}

fun findPaths2D(grid: Array<Array<Int>>, start: Pair<Int, Int>, goal: Pair<Int, Int>): List<List<Pair<Int, Int>>> {
    val directions = listOf(
        Pair(1, 0), Pair(-1, 0), // Rechts, Links
        Pair(0, 1), Pair(0, -1)  // Oben, Unten
    )

    val openSet = PriorityQueue<Node>()
    val bestPaths = mutableMapOf<Pair<Int, Int>, MutableList<Node>>()

    val startNode = Node(start.first, start.second, 0, heuristic(start.first, start.second, goal.first, goal.second))
    openSet.add(startNode)
    bestPaths[start] = mutableListOf(startNode)

    while (openSet.isNotEmpty()) {
        val current = openSet.poll()
        val currentPos = Pair(current.x, current.y)

        if (currentPos == goal) continue // Ziel erreicht, aber alle Optionen prüfen

        for (dir in directions) {
            val neighborPos = Pair(current.x + dir.first, current.y + dir.second)
            if (neighborPos.first !in grid.indices ||
                neighborPos.second !in grid[0].indices ||
                grid[neighborPos.first][neighborPos.second] == 1
            ) continue // Überspringe ungültige Positionen

            val g = current.g + 1 // Kosten erhöhen
            val h = heuristic(neighborPos.first, neighborPos.second, goal.first, goal.second)
            val neighborNode = Node(neighborPos.first, neighborPos.second, g, h, current)

            if (!bestPaths.containsKey(neighborPos) || g <= bestPaths[neighborPos]!!.minOf { it.g }) {
                openSet.add(neighborNode)
                bestPaths.computeIfAbsent(neighborPos) { mutableListOf() }.add(neighborNode)
            }
        }
    }

    // Rekonstruiere alle besten Pfade
    fun backtrack(node: Node?): List<List<Pair<Int, Int>>> {
        if (node == null) return listOf(emptyList())
        val paths = mutableListOf<List<Pair<Int, Int>>>()
        for (parent in bestPaths[Pair(node.x, node.y)]?.mapNotNull { it.parent } ?: emptyList()) {
            paths += backtrack(parent).map { it + Pair(node.x, node.y) }
        }
        return if (paths.isEmpty()) listOf(listOf(Pair(node.x, node.y))) else paths
    }

    return backtrack(bestPaths[goal]?.firstOrNull()).map { it.reversed() }
}

// Beispielnutzung
fun main() {
    val grid = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(1, 1, 0, 1),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 1, 1, 0)
    ) // 4x4 Gitter mit einigen Hindernissen
    val start = Pair(0, 0)
    val goal = Pair(3, 3)

    val paths = findPaths2D(grid, start, goal)
    println("Alle besten Pfade:")
    for (path in paths) {
        println(path)
    }
}