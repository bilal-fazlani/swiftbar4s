package com.bilalfazlani.swiftbar4s.parser
import com.bilalfazlani.swiftbar4s.dsl.*
import com.bilalfazlani.swiftbar4s.parser.Printer
import com.bilalfazlani.swiftbar4s.models.MenuItem.Menu

open class MenuRenderer(parser: Parser, printer: Printer) {
  def renderMenu(menu: Menu, streaming: Boolean): Unit = {
    if (streaming) printer.println("~~~")
    parser
      .parse(menu)
      .lines
      .foreach(printer.println)
  }
}
