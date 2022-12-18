import Solution.Shape.{Paper, Rock}

import scala.io.Source

object SolutionDay2 {

  def main(args: Array[String]): Unit = {
    val data = getData("input_day_2.txt")

    val resultA = partA(data)
    val resultB = partB(data)

    println(s"Result A: $resultA")
    println(s"Result B: $resultB")
  }

  def getData(filePath: String): List[String] = {
    val source = Source.fromFile(filePath)
    val data = source.getLines().toList
    source.close()
    data
  }

  enum Shape {
    case Rock, Paper, Scissors

    def lower(): Shape = this match
      case Rock => Scissors
      case Paper => Rock
      case Scissors => Paper

    def higher(): Shape = this match
      case Rock => Paper
      case Paper => Scissors
      case Scissors => Rock
  }

  def shapeFromChar(c: Char): Shape = c match
    case 'A' | 'X' => Shape.Rock
    case 'B' | 'Y' => Shape.Paper
    case 'C' | 'Z' => Shape.Scissors

  def getShapeScore(shape: Shape): Int = shape match
    case Shape.Rock => 1
    case Shape.Paper => 2
    case Shape.Scissors => 3


  def getPlayScore(my: Shape, their: Shape): Int = (my, their) match
    case (x, y) if x == y => 3
    case (x, y) if x == y.higher() => 6
    case _ => 0

  def evalRound(myPlay: Shape, theirPlay: Shape): Int =
    getShapeScore(myPlay) + getPlayScore(myPlay, theirPlay)

  def getMyShape(their: Shape, command: Char): Shape = command match
    case 'X' => their.lower()
    case 'Y' => their
    case 'Z' => their.higher()

  def playRoundPartA(line: String): Int = {
    val theirPlay = shapeFromChar(line.charAt(0))
    val myPlay = shapeFromChar(line.charAt(2))
    evalRound(myPlay, theirPlay)
  }

  def playRoundPartB(line: String): Int = {

    val theirPlay = shapeFromChar(line.charAt(0))
    val myPlay = getMyShape(theirPlay, line.charAt(2))

    evalRound(myPlay, theirPlay)
  }

  def partA(data: List[String]): Int = {
    data.map(playRoundPartA).sum
  }

  def partB(data: List[String]): Int = {
    data.map(playRoundPartB).sum
  }
}
