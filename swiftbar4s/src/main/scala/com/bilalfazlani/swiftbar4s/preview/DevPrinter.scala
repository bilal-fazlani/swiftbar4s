package com.bilalfazlani.swiftbar4s.preview

import sys.process._
import better.files._
import com.bilalfazlani.swiftbar4s.parser.Printer

class DevPrinter(filePath: String) extends Printer {
  private var cleaned = false

  private[bilalfazlani] val file = filePath.toFile

  override def println(str: String) =
    if (file.exists && !cleaned) {
      file.delete()
      cleaned = true
    }
    file.appendLine(str)
}