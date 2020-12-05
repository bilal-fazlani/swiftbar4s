package tech.bilal.bitbar4s

import tech.bilal.bitbar4s.models.MenuItem
import tech.bilal.bitbar4s.parser.Parser

abstract class BitBarApp {
  def app: MenuItem

  def main(args: Array[String]): Unit =
    new Parser().parse(app).lines.foreach(println)
}
