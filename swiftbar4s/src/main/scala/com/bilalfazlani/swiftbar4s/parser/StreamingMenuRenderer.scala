package com.bilalfazlani.swiftbar4s.parser
import com.bilalfazlani.swiftbar4s.dsl.*
import com.bilalfazlani.swiftbar4s.parser.Printer
import com.bilalfazlani.swiftbar4s.models.MenuItem.Menu
import com.bilalfazlani.swiftbar4s.models.MenuItem

open class StreamingMenuRenderer(parser: Parser, printer: Printer) {
  def renderMenu(menuItem: MenuItem, streaming: Boolean): Unit = {
    if (streaming) printer.println("~~~")
    parser.parse(menuItem).lines.foreach(printer.println)
  }
}
