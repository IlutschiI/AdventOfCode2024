package org.example.day02

import org.example.readAsString
import kotlin.math.absoluteValue

fun main() {
    val input = "day02/input".readAsString()

    val safeReports = input.split(System.lineSeparator()).filter {
        val report = it.split(Regex("\\s+")).map(String::toInt)

        val levelDistance =            getLevelDistance(report)
        val inOrDecreasing = levelDistance.all { level -> level > 0 } || levelDistance.all { level -> level < 0 }
        val maxDistanceLower3Greater1 =            getMaxDistanceLower3Greater1(levelDistance)
        inOrDecreasing && maxDistanceLower3Greater1
    }.size

    val safeReportsPart2 = input.split(System.lineSeparator()).filter {
        val report = it.split(Regex("\\s+")).map(String::toInt)

        for (i in report.indices) {
            val alternateReport = report.toMutableList().apply { removeAt(i) }
            val levelDistance = getLevelDistance(alternateReport)
            val inOrDecreasing = getInOrDecreasing(levelDistance)
            val maxDistanceLower3Greater1 = getMaxDistanceLower3Greater1(levelDistance)
            if (inOrDecreasing && maxDistanceLower3Greater1){
                return@filter true
            }
        }
        val levelDistance = getLevelDistance(report)
        val inOrDecreasing = getInOrDecreasing(levelDistance)
        val maxDistanceLower3Greater1 = getMaxDistanceLower3Greater1(levelDistance)
        inOrDecreasing && maxDistanceLower3Greater1
    }.size

    println(safeReports)
    println(safeReportsPart2)
}

private fun getLevelDistance(report: List<Int>) =
    report.mapIndexed { index, i -> report.getOrNull(index + 1)?.let { level -> i - level } }.filterNotNull()

private fun getMaxDistanceLower3Greater1(levelDistance: List<Int>) =
    levelDistance.maxOf { level -> level.absoluteValue } <= 3 && levelDistance.minOf { level -> level.absoluteValue } >= 1

private fun getInOrDecreasing(levelDistance: List<Int>) =
    levelDistance.all { level -> level > 0 } || levelDistance.all { level -> level < 0 }