package reductions

import common._
import org.scalameter._

object ParallelParenthesesBalancingRunner {

  val standardConfig = config(
    Key.exec.minWarmupRuns -> 40,
    Key.exec.maxWarmupRuns -> 80,
    Key.exec.benchRuns -> 120,
    Key.verbose -> true
  ) withWarmer (new Warmer.Default)
  @volatile var seqResult = false
  @volatile var parResult = false

  def main(args: Array[String]): Unit = {
    val length = 100000000
    val chars = new Array[Char](length)
    val threshold = 10000
    val seqtime = standardConfig measure {
      seqResult = ParallelParenthesesBalancing.balance(chars)
    }
    println(s"sequential result = $seqResult")
    println(s"sequential balancing time: $seqtime ms")

    val fjtime = standardConfig measure {
      parResult = ParallelParenthesesBalancing.parBalance(chars, threshold)
    }
    println(s"parallel result = $parResult")
    println(s"parallel balancing time: $fjtime ms")
    println(s"speedup: ${seqtime / fjtime}")
  }
}

object ParallelParenthesesBalancing {

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def balance(chars: Array[Char]): Boolean = {
    def balanceRec(chars: List[Char], acc: Int): Boolean = chars match {
      case Nil          => acc == 0
      case _ if acc < 0 => false
      case '(' :: tail  => balanceRec(tail, acc + 1)
      case ')' :: tail  => balanceRec(tail, acc - 1)
      case _ :: tail    => balanceRec(tail, acc)
    }

    balanceRec(chars.toList, 0)
  }

  /** Returns `true` iff the parentheses in the input `chars` are balanced.
    */
  def parBalance(chars: Array[Char], threshold: Int): Boolean = {

    def traverse(idx: Int, until: Int, arg1: Int, arg2: Int): (Int, Int) = {
      if (idx < until) {
        chars(idx) match {
          case '('             => traverse(idx + 1, until, arg1 + 1, arg2)
          case ')' if arg1 > 0 => traverse(idx + 1, until, arg1 - 1, arg2)
          case ')'             => traverse(idx + 1, until, arg1, arg2 + 1)
          case _               => traverse(idx + 1, until, arg1, arg2)
        }
      } else (arg1, arg2)
    }

    def reduce(from: Int, until: Int): (Int, Int) = {
      if (until - from <= threshold) traverse(from, until, 0, 0)
      else {
        val mid = from + (until - from) / 2
        val ((a1, a2), (b1, b2)) =
          parallel(reduce(from, mid), reduce(mid, until))

        if (a1 > b2) {
          (a1 - b2 + b1, a2)
        } else {
          (b1, b2 - a1 + a2)
        }
      }
    }

    reduce(0, chars.length) == (0, 0)
  }

  // For those who want more:
  // Prove that your reduction operator is associative!

}
