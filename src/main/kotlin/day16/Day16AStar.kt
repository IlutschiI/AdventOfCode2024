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
        gCost: Int = Int.MAX_VALUE,
        hCost: Int = Int.MAX_VALUE,
        parent: Node? = null
    ) : this(x, y) {
        this.direction = direction
        this.parent = parent
        this.gCost = gCost
        this.hCost = hCost
    }

    lateinit var direction: Direction
    var gCost: Int = Int.MAX_VALUE
    var hCost: Int = Int.MAX_VALUE
    var parent: Node? = null

    val fCost: Int
        get() = gCost + hCost

    override fun compareTo(other: Node): Int {
        return fCost.compareTo(other.fCost)
    }

}

fun aStar(
    map: List<List<Char>>,
    startPosition: Pair<Int, Int>,
    direction: Direction,
    endPosition: Pair<Int, Int>
): Int {
    val startNode =
        Node(startPosition.second, startPosition.first, direction, 0, heuristic(startPosition, endPosition))

    val fringe = PriorityQueue<Node>()
    val visited = mutableSetOf<Node>()

//    val possiblePaths = mutableListOf<Node>()

    fringe.add(startNode)

    while (fringe.isNotEmpty()) {
        val currentNode = fringe.poll()
        visited.add(currentNode)

        if (currentNode.x == endPosition.second && currentNode.y == endPosition.first) {
//            if (possiblePaths.isEmpty()){
//                //add
//            } else if (possiblePaths.first().gCost == currentNode.gCost) {
//                //add
//            } else {
//                //return list
//            }
            return currentNode.gCost
        }

        for (neighbor in getNeighbors(map, currentNode)) {
            if (neighbor !in visited) {
                neighbor.gCost =
                    if (currentNode.direction != neighbor.direction) currentNode.gCost + 1001 else currentNode.gCost + 1
                neighbor.hCost = heuristic(neighbor.y to neighbor.x, endPosition)
                fringe.add(neighbor)
                visited.add(neighbor)

            }
        }
    }
    return -1
}

//fun isNodeAlreadyInPath(node:Node): Boolean{
//    var parent = node.parent
//    while (parent != null){
//        if (parent.x == node.x && parent.y == node.y){
//            return true
//        }
//        parent = parent.parent
//    }
//    return false
//}

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
//    val input = "day16/test_input".readAsString()
//    val input = "day16/test_input2".readAsString()
    val input = "day16/input".readAsString()

    val map = input.split(System.lineSeparator()).map { it.toCharArray().toList() }
    val startPosition = getFirstPositionOf(map, 'S')
    val endPosition = getFirstPositionOf(map, 'E')

    aStar(map, startPosition, Direction.RIGHT, endPosition).let {
        println(it)
    }

}