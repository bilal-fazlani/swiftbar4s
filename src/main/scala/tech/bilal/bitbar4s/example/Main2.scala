package tech.bilal.bitbar4s.example

import tech.bilal.bitbar4s.BitBarApp
import tech.bilal.bitbar4s.models.Attribute._
import tech.bilal.bitbar4s.models.MenuItem._

object Main2 extends BitBarApp {
  override val echoMode = true

  override val app = ShellCommand(
    "command",
    "echo",
    Seq("hello")
  )
}
