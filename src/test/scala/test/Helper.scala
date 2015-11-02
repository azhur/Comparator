package test

import java.io.File

import comparator.ObjectComparator.ComparisonError
import comparator.{Comparator, LENIENT, Mode, STRICT}
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

trait Helper {

  this: FunSuite with Matchers =>

  var mode:Mode = _
  def useStrict():Unit = mode = STRICT
  def useLenient():Unit = mode = LENIENT

  val resDir = new File(System.getProperty("user.dir"), "/src/test/fs")
  require(resDir.exists())

  def file(name: String): File = new File(resDir, name)

  def toString(file: File): String = {
    val s = Source.fromFile(file)
    try {
      s.mkString
    } catch {
      case e: Throwable =>
        s.close()
        throw e
    }
  }

  def compare(expected: String, actual: String):Unit = {
    val e = toString(file(expected))
    val a = toString(file(actual))

    Comparator(mode).compare(e, a)
  }

  def compareJson(path: String):Unit = compareType("json", path)
  def compareXml(path: String):Unit = compareType("xml", path)
  def compareCss(path: String):Unit = compareType("css", path)
  def compareTxt(path: String):Unit = compareType("txt", path)

  def errorJson(n: Int, msg: String):Unit = test(s"Json error $n") {
    intercept[ComparisonError] {
      compareJson(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorXml(n: Int, msg: String):Unit = test(s"Xml error $n") {
    intercept[ComparisonError] {
      compareXml(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorCss(n: Int, msg: String):Unit = test(s"Css error $n") {
    intercept[ComparisonError] {
      compareCss(s"/error/$n/")
    }.msg shouldBe msg
  }

  def errorTxt(n: Int, msg: String):Unit = test(s"Txt error $n") {
    intercept[ComparisonError] {
      compareTxt(s"/error/$n/")
    }.msg shouldBe msg
  }

  def okJson(n: Int):Unit = test(s"Json ok $n") {
    compareJson(s"/ok/$n/")
  }

  def okXml(n: Int):Unit = test(s"Xml ok $n") {
    compareXml(s"/ok/$n/")
  }

  def okCss(n: Int):Unit = test(s"Css ok $n") {
    compareCss(s"/ok/$n/")
  }

  def okTxt(n: Int):Unit = test(s"Txt ok $n") {
    compareTxt(s"/ok/$n/")
  }

  def compareType(tpe: String, path: String):Unit = {
    val template = tpe + {
      mode match {
        case STRICT => "/strict/"
        case LENIENT => "/lenient/"
      }
    } + path + "%s." + tpe
    compare(template.format("expected"), template.format("actual"))
  }

}
