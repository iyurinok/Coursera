package quickcheck

import java.lang.Math._

import common._

import org.scalacheck._
import Arbitrary._
import Gen._
import Prop._

abstract class QuickCheckHeap extends Properties("Heap") with IntHeap {

  lazy val genHeap: Gen[H] = for {
    a <- arbitrary[Int]
    h <- oneOf(const(empty), genHeap)
  } yield {
    insert(a, h)
  }

  implicit lazy val arbHeap: Arbitrary[H] = Arbitrary(genHeap)

  property("gen1") = forAll { (h: H) =>
    val m = if (isEmpty(h)) 0 else findMin(h)
    findMin(insert(m, h)) == m
  }

  property("min1") = forAll { a: Int =>
    val h = insert(a, empty)
    findMin(h) == a
  }

  property("min2") = forAll { (a1: Int, a2: Int) =>
    val h = insert(a1, insert(a2, empty))
    findMin(h) == min(a1, a2)
  }

  property("del1") = forAll { (a1: Int, a2: Int) =>
    val h = insert(a1, insert(a2, empty))
    findMin(deleteMin(h)) == Math.max(a1, a2)
  }

  property("min3") = forAll { (h: H) =>
    val m = findMin(h)
    findMin(insert(m, h)) == m
  }

  property("del2") = forAll { a: Int =>
    val h = insert(a, empty)
    val h1 = deleteMin(h)
    h1 == empty
  }

  property("meld1") = forAll { (h1: H, h2: H) =>
    findMin(meld(h1, h2)) == min(findMin(h1), findMin(h2))
  }

  property("meld") = forAll { (a: H, b: H) =>
    val min = findMin(meld(a, b))
    min == scala.math.min(findMin(a), findMin(b))
  }

  property("meld2") = forAll { a: Int =>
    val h = insert(a, empty)
    isEmpty(deleteMin(meld(h, empty)))
  }

  property("del3") = forAll { (a: Int, b: Int, c: Int) =>
    val h = insert(c, insert(b, insert(a, empty)))
    findMin(deleteMin(deleteMin(h))) == max(c, max(a, b))
  }


}
