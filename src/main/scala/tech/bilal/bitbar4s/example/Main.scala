package tech.bilal.bitbar4s.example

import tech.bilal.bitbar4s.BitBarApp
import tech.bilal.bitbar4s.dsl.BitBarDsl
import tech.bilal.bitbar4s.models.MenuItem

object Main extends BitBarApp with BitBarDsl {

  override val handler: Handler = {
    case (action, metaData) =>
      println(action)
      println(metaData)
  }
  override val pluginName: String = "my-plugin"

  override val menu: MenuItem = text("bilal")
    .color("red")
    .textSize(20) >>
    (
      dispatchAction("print something", "print", "hello world")
        .showTerminal(),
      text("item 1")
        .font("Times"),
      text("item 2")
        .textSize(15),
      text("submenu") >>
        (
          text("item 3"),
          text("item 4"),
          text("nested").color("orange") >>
            (
              text("item 5"),
              text("item 6"),
              shellCommand("item 7", "echo", "hello world")
                .showTerminal()
          )
      )
  )
}
