package tech.bilal.bitbar4s.example

import tech.bilal.bitbar4s.BitBarApp
import tech.bilal.bitbar4s.models.MenuItem._

object Main extends BitBarApp {
  override val menu: Menu = Menu(
    Text("MyApp"),
    Seq(
      Text("Item 1"),
      Text("Item 2"),
      Menu(
        Text("Submenu"),
        Seq(
          Text("Item 3"),
          Text("Item 4"),
          Menu(
            Text("Nested"),
            Seq(
              Text("Item 5"),
              Text("Item 6")
            )
          )
        )
      )
    )
  )
}
