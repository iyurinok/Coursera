package recfun

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
    * Exercise 1
    */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
    * Exercise 2
    */
  def balance(chars: List[Char]): Boolean = {
    @tailrec
    def balanceWithAcc(acc: Int, chars: List[Char]): Boolean =
      if (acc < 0) false
      else if (chars.isEmpty) acc == 0
      else chars.head match {
        case '(' => balanceWithAcc(acc + 1, chars.tail)
        case ')' => balanceWithAcc(acc - 1, chars.tail)
        case _ => balanceWithAcc(acc, chars.tail)
      }

    balanceWithAcc(0, chars)
  }

  /**
    * Exercise 3
    */
  def countChange(money: Int, coins: List[Int]): Int = {
    def countChangeWithCoin(coin: Int, coins: List[Int]): Int = {
      if (money < coin) 0
      else countChange(money - coin, coins)
    }

    if (money == 0) 1
    else if (coins.isEmpty) 0
    else countChangeWithCoin(coins.head, coins) + countChange(money, coins.tail)
  }
}
