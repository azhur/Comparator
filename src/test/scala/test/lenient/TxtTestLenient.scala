package test.lenient

import org.scalatest.{FunSuite, Matchers}
import test.Helper

class TxtTestLenient extends FunSuite with Matchers with Helper {
   useLenient()

   Seq(
     1 -> "Content doesn't match",
     2 -> "Line Some text 123 should match [a-zA-Z]"
   ).foreach((e) => errorTxt(e._1, e._2))

   (1 to 2) foreach okTxt

 }
