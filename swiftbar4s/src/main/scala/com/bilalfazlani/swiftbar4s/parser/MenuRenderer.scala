package com.bilalfazlani.swiftbar4s.parser
import com.bilalfazlani.swiftbar4s.dsl.*
import com.bilalfazlani.swiftbar4s.parser.Printer

open class MenuRenderer(parser: Parser, printer: Printer) {
  def renderMenu(menuBuilder: MenuBuilder, streaming: Boolean): Unit = {
    if (streaming) printer.println("~~~")
    parser
      .parse(menuBuilder.build)
      .lines
      .foreach(printer.println)
  }
}
