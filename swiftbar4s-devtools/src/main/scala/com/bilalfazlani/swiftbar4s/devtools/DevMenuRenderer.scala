package com.bilalfazlani.swiftbar4s.devtools

import com.bilalfazlani.swiftbar4s.dsl.MenuBuilder
import com.bilalfazlani.swiftbar4s.models.MenuItem.Menu
import com.bilalfazlani.swiftbar4s.parser.MenuRenderer
import com.bilalfazlani.swiftbar4s.parser.Parser
import java.nio.file.attribute.PosixFilePermission

class DevMenuRenderer(parser: Parser, printer: DevPrinter) extends MenuRenderer(parser, printer) {
  override def renderMenu(menu: Menu, streaming: Boolean): Unit = {
    printer.println("#!/bin/bash -e")
    printer.println("")
    printer.println("cat << EOF")
    parser
        .parse(menu)
        .lines
        .foreach(printer.println)
    printer.println("EOF")
    printer.file.addPermission(PosixFilePermission.OTHERS_EXECUTE)
    printer.file.addPermission(PosixFilePermission.OWNER_EXECUTE)
  } 
}