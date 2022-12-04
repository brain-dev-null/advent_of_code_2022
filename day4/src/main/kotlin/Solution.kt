import java.io.File
import java.util.function.Predicate

typealias Range = Pair<Int, Int>
typealias RangePair = Pair<Range, Range>

fun main(args: Array<String>) {
    val data = File("input.txt").useLines { it.toList() }

    val resultA = partA(data)
    val resultB = partB(data)

    println("Result A: $resultA")
    println("Result B: $resultB")
}

fun partA(data: List<String>): Int =
    parseDataToRanges(data)
        .filter(::enclosedRange)
        .size

fun partB(data: List<String>): Int =
    parseDataToRanges(data)
        .filter(::overlappingRange)
        .size

fun enclosedRange(rangePair: RangePair): Boolean =
    testBothWays(::encloses).test(rangePair)

fun overlappingRange(rangePair: RangePair): Boolean =
    testBothWays(::overlaps).test(rangePair)

fun encloses(outer: Range, inner: Range): Boolean =
    outer.first <= inner.first && inner.second <= outer.second

fun overlaps(rangeA: Range, rangeB: Range): Boolean =
    rangeA.second >= rangeB.first && rangeA.second <= rangeB.second

fun testBothWays(predicate: (Range, Range) -> Boolean): Predicate<RangePair> =
    Predicate {
        predicate(it.first, it.second) || predicate(it.second, it.first)
    }

fun toRange(raw: String): Range =
    raw.split("-")
        .let { Pair(it.first().toInt(), it.last().toInt()) }

fun parseDataToRanges(data: List<String>): List<RangePair> =
    data.map { it.split(",").map(::toRange) }
        .map { Pair(it.first(), it.last())}
