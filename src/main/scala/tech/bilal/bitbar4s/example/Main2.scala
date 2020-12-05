package tech.bilal.bitbar4s.example

import tech.bilal.bitbar4s.BitBarApp
import tech.bilal.bitbar4s.models.MenuItem
import tech.bilal.bitbar4s.models.MenuItem._

object Main2 extends BitBarApp {
  override val echoMode = true

  override val app: MenuItem = ShellCommand(
    "command",
    "echo",
    Seq("hello")
  )
}
