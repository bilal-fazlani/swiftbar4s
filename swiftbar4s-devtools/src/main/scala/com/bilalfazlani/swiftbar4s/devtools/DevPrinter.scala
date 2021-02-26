package com.bilalfazlani.swiftbar4s.devtools

import sys.process.*
import better.files.*
import com.bilalfazlani.swiftbar4s.parser.Printer

class DevPrinter(filePath: String) extends Printer {
  private var cleaned = false

  private[swiftbar4s] val file = filePath.toFile

  override def println(str: String) =
    if (file.exists && !cleaned) {
      file.delete()
      cleaned = true
    }
    file.appendLine(str)
}