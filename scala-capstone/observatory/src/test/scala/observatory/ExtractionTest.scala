package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

trait ExtractionTest extends FunSuite {

  test("stations") {
    val dataset = Extraction.stations("/stations.csv")
    val stations = dataset.collect()

    assert(stations.nonEmpty)
  }

}
