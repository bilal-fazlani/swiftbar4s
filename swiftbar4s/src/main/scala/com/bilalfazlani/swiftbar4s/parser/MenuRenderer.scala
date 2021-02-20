package com.bilalfazlani.swiftbar4s.parser
import com.bilalfazlani.swiftbar4s.dsl._
import com.bilalfazlani.swiftbar4s.parser.Printer

class MenuRenderer(parser: Parser, printer: Printer) {
  def renderMenu(menuBuilder: MenuBuilder, streaming: Boolean): Unit = {
    if (streaming) printer.println("~~~")
    parser
      .parse(menuBuilder.build)
      .lines
      .foreach(printer.println)
  }
}
