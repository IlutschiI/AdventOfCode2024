package org.example.day16

import org.example.readAsString
import java.util.*
import kotlin.math.absoluteValue

data class Node(
    val x: Int,
    val y: Int,
) : Comparable<Node> {

    constructor(
        x: Int,
        y: Int,
        direction: Direction,
        hCost: Int = Int.MAX_VALUE,
        parent: Node? = null
    ) : this(x, y) {
        this.direction = direction
        this.parent = parent
        this.hCost = hCost
    }

    lateinit var direction: Direction
    val gCost: Int
        get() {
            return if (parent == null) {
                0
            } else if (parent!!.direction == direction) parent!!.gCost + 1 else parent!!.gCost + 1001
        }
    var hCost: Int = Int.MAX_VALUE
    var parent: Node? = null

    val fCost: Int
        get() = gCost + hCost

    override fun compareTo(other: Node): Int {
        return fCost.compareTo(other.fCost)
    }

}

fun aStarPart2(
    map: List<List<Char>>,
    startPosition: Pair<Int, Int>,
    direction: Direction,
    endPosition: Pair<Int, Int>
): List<List<Pair<Int, Int>>> {
    val startNode =
        Node(startPosition.second, startPosition.first, direction, heuristic(startPosition, endPosition))

    val fringe = PriorityQueue<Node>()

    val bestPaths = mutableMapOf<Pair<Int, Int>, MutableList<Node>>()

    fringe.add(startNode)
    bestPaths[startPosition] = mutableListOf(startNode)


    while (fringe.isNotEmpty()) {
        val currentNode = fringe.poll()

        if (currentNode.x == endPosition.second && currentNode.y == endPosition.first) {
            continue
        }

        for (neighbor in getNeighbors(map, currentNode)) {
            neighbor.hCost = heuristic(neighbor.y to neighbor.x, endPosition)
            if (!bestPaths.containsKey(neighbor.y to neighbor.x) || neighbor.gCost <= bestPaths[neighbor.y to neighbor.x]!!.minOf { it.gCost }) {
                fringe.add(neighbor)
                bestPaths.computeIfAbsent(neighbor.y to neighbor.x) { mutableListOf() }.add(neighbor)
            }
        }
    }

    // Rekonstruiere alle besten Pfade
    fun backtrack(node: Node?): List<List<Pair<Int, Int>>> {
        if (node == null) return listOf(emptyList())
        val paths = mutableListOf<List<Pair<Int, Int>>>()
        for (parent in bestPaths[Pair(node.y, node.x)]?.mapNotNull { it.parent } ?: emptyList()) {
            paths += backtrack(parent).map { it + Pair(node.y, node.x) }
        }
        return if (paths.isEmpty()) listOf(listOf(Pair(node.y, node.x))) else paths
    }

    return backtrack(bestPaths[endPosition]?.firstOrNull()).map { it.reversed() }
}


fun aStar(
    map: List<List<Char>>,
    startPosition: Pair<Int, Int>,
    direction: Direction,
    endPosition: Pair<Int, Int>
): List<Node> {
    val startNode =
        Node(startPosition.second, startPosition.first, direction, heuristic(startPosition, endPosition))

    val fringe = PriorityQueue<Node>()
    val visited = mutableSetOf<Node>()

    fringe.add(startNode)

    while (fringe.isNotEmpty()) {
        val currentNode = fringe.poll()
        visited.add(currentNode)

        if (currentNode.x == endPosition.second && currentNode.y == endPosition.first) {
            return reconstructPath(currentNode)
        }

        for (neighbor in getNeighbors(map, currentNode)) {
            if (neighbor !in visited) {
                neighbor.hCost = heuristic(neighbor.y to neighbor.x, endPosition)
                fringe.add(neighbor)

            }
        }
    }
    return emptyList()
}

fun reconstructPath(goalNode: Node): List<Node> {
    val path = mutableListOf<Node>()
    var currentNode: Node? = goalNode
    while (currentNode != null) {
        path.add(currentNode)
        currentNode = currentNode.parent
    }
    return path.reversed()
}


fun getNeighbors(map: List<List<Char>>, node: Node): List<Node> {
    val directions = listOf(
        node.direction,
        node.direction.next(),
        node.direction.previous(),
    )
    val neighbors = mutableListOf<Node>()
    for (dir in directions) {
        val newX = node.x + dir.xDiff
        val newY = node.y + dir.yDiff
        if (map[newY][newX] != '#') {
            neighbors.add(Node(newX, newY, dir, parent = node))
        }
    }

    return neighbors
}

fun heuristic(startPosition: Pair<Int, Int>, endPosition: Pair<Int, Int>) =
    (startPosition.first - endPosition.first).absoluteValue + (startPosition.second - endPosition.second).absoluteValue

fun getFirstPositionOf(map: List<List<Char>>, char: Char): Pair<Int, Int> {
    map.forEachIndexed { rowIndex, chars ->
        chars.forEachIndexed { columnIndex, c ->
            if (c == char) return rowIndex to columnIndex
        }
    }
    error("Start not found")
}

fun main() {
    val input = "day16/test_input".readAsString()
//    val input = "day16/test_input2".readAsString()
//    val input = "day16/input".readAsString()

    val map = input.split(System.lineSeparator()).map { it.toCharArray().toList() }
    val startPosition = getFirstPositionOf(map, 'S')
    val endPosition = getFirstPositionOf(map, 'E')

    val path = aStar(map, startPosition, Direction.RIGHT, endPosition).let {
        println(it.last().gCost)
        it
    }


    aStarPart2(map, startPosition, Direction.RIGHT, endPosition).let {
        println(it.flatMap { it }.distinct().size)
    }
}