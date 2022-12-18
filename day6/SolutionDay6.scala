import scala.io.Source

object SolutionDay6 {

  def main(args: Array[String]): Unit = {
    val source = Source.fromFile("input.txt")
    val data = source.getLines().toList
    source.close()

    val resultA = partA(data)
    val resultB = partB(data)

    println(s"Result A: $resultA")
    println(s"Result B: $resultB")
  }

  def partA(data: List[String]): String =
    (windows(data.head, 4).map(uniqueElements).indexOf(4) + 4).toString

  def partB(data: List[String]): String =
    (windows(data.head, 14).map(uniqueElements).indexOf(14) + 14).toString

  def windows[A](list: Iterable[A], size: Int): List[List[A]] =
    list.sliding(size).map(_.toList).toList

  def uniqueElements[A](as: Iterable[A]): Int = as.toSet.size
}