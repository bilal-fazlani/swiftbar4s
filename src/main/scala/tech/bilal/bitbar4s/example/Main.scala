package tech.bilal.bitbar4s.example

import tech.bilal.bitbar4s.models.MenuItem._
import tech.bilal.bitbar4s.parser.Parser

object Main extends App {
  val menu = Menu(
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

  val output = new Parser().parse(menu)

  output.lines.foreach(println)
}
