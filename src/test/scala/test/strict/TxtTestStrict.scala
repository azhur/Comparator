package test.strict

import org.scalatest.{FunSuite, Matchers}
import test.Helper

class TxtTestStrict extends FunSuite with Matchers with Helper {
  useStrict()

  Seq(
    1 -> "Content is not equal and can't be associate with json/xml. Unable to compare"
  ).map((e) => errorTxt(e._1, e._2))

  (1 to 1) map okTxt

}
