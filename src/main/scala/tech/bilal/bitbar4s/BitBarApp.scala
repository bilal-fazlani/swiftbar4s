package tech.bilal.bitbar4s

import tech.bilal.bitbar4s.models.MenuItem.Menu
import tech.bilal.bitbar4s.parser.Parser

abstract class BitBarApp {
  def menu: Menu

  def main(args: Array[String]): Unit =
    new Parser().parse(menu).lines.foreach(println)
}
