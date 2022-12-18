import java.io.File


class Stack<T> {
    val elements: MutableList<T> = mutableListOf()

    fun push(t: T): Unit {
        this.elements.add(t)
    }
    fun pop(): T? = this.elements.removeLastOrNull()
    fun peek(): T? = this.elements.lastOrNull()

    override fun toString(): String = this.elements.toString()

}

fun <T> emptyStack(): Stack<T> = Stack()

fun stackToPosition(i: Int): Int = (i - 1) * 4 + 1

fun main(args: Array<String>) {
    val data = File("input.txt").readLines()

    val resultA = partA(data)
    val resultB = partB(data)

    println("Result A: $resultA")
    println("Result B: $resultB")
}

data class Command(val source: Int, val destination: Int, val amount: Int)

fun parseCommand(line: String): Command =
    line.replace("move ", "")
        .replace("from ", "",)
        .replace("to ", "")
        .split(" ")
        .let { Command(it[1].toInt(), it[2].toInt(), it[0].toInt()) }

fun partA(data: List<String>): String {
    val rawState = data.takeWhile { it.isNotEmpty() }
    val commands = data.dropWhile { it.isNotEmpty() }.drop(1).map { parseCommand(it) }
    val numberOfStacks = rawState.last().split(Regex("\\s+")).size - 1

    val stacks: Map<Int, Stack<Char>> = (1..numberOfStacks).associate { it to emptyStack() }


    rawState.reversed().drop(1)
        .forEach { line -> (1..numberOfStacks)
            .mapNotNull { index -> line.elementAtOrNull(stackToPosition(index))?.let { if (it.isWhitespace()) null else index to it } }
            .forEach { stacks[it.first]?.push(it.second) }
        }


    commands.forEach { command ->  (1..command.amount).mapNotNull { stacks[command.source]!!.pop()}.forEach { element -> stacks[command.destination]!!.push(element) }}

    val result = stacks.map { it.value.peek() }.joinToString("")

    return result
}

fun partB(data: List<String>): String {
    val rawState = data.takeWhile { it.isNotEmpty() }

    val commands = data.dropWhile { it.isNotEmpty() }.drop(1).map { parseCommand(it) }
    val numberOfStacks = rawState.last().split(Regex("\\s+")).size - 1

    val stacks: Map<Int, Stack<Char>> = (1..numberOfStacks).associate { it to emptyStack() }


    rawState.reversed().drop(1)
        .forEach { line -> (1..numberOfStacks)
            .mapNotNull { index -> line.elementAtOrNull(stackToPosition(index))?.let { if (it.isWhitespace()) null else index to it } }
            .forEach { stacks[it.first]?.push(it.second) }
        }


    commands.forEach { command ->  (1..command.amount).mapNotNull { stacks[command.source]!!.pop()}.reversed().forEach { element -> stacks[command.destination]!!.push(element) }}

    val result = stacks.map { it.value.peek() }.joinToString("")

    return result
}
