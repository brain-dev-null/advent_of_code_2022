import java.nio.file.{FileSystem, FileSystems}
import scala.io.Source
import scala.io.StdIn.readLine

object SolutionDay1 {

  def main(args: Array[String]): Unit = {
    val data = getData("input_day_1.txt")

    val resultA = partA(data)
    val resultB = partB(data)

    println(s"Result A: $resultA")
    println(s"Result B: $resultB")
  }

  def getData(filePath: String): List[String] = {
    val source = Source.fromFile(filePath)
    val data = source.getLines()
    source.close()
    data.toList
  }

  def splitAtEmptyLine(data: List[String]): List[List[String]] =
    data.foldLeft(List(List.empty): List[List[String]])((list, line) => (line, list) match {
      case ("", _) => List.empty :: list
      case (_, head :: tail) => (line :: head) :: tail
    })

  def getCalorieSums(data: List[String]): List[Int] =
    splitAtEmptyLine(data).map(_.map(_.toInt).sum)

  def partA(data: List[String]): Int =
    getCalorieSums(data).max

  def partB(data: List[String]): Int =
    getCalorieSums(data).sorted.reverse.take(3).sum
}
