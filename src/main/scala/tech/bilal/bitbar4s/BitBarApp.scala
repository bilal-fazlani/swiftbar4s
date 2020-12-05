package tech.bilal.bitbar4s

import tech.bilal.bitbar4s.models.MenuItem
import tech.bilal.bitbar4s.parser.Parser

abstract class BitBarApp {
  def app: MenuItem

  val echoMode: Boolean = false

  def printF(str: String): Unit = {
    val st = if (!echoMode) str else s"""echo "$str""""
    println(st)
  }

  def main(args: Array[String]): Unit =
    new Parser().parse(app).lines.foreach(printF)
}
