package tech.bilal.bitbar4s.example

import tech.bilal.bitbar4s.BitBarApp
import tech.bilal.bitbar4s.models.Attribute._
import tech.bilal.bitbar4s.models.MenuItem._

object Main extends BitBarApp {

  override def handler(action: String, metadata: Option[String]): Unit = {
    println(s"""
         |ACTION: $action
         |METADATA: $metadata
         |""".stripMargin)
  }

  override val app: Menu = Menu(
    Text("MyApp", attributes = Set(Color("red"), TextSize(20))),
    Seq(
      DispatchAction(
        "print something",
        "print",
        Some("hello world"),
        terminal = true
      ),
      Text("Item 1", attributes = Set(Font("Times New Roman"))),
      Text("Item 2"),
      Menu(
        Text("Submenu"),
        Seq(
          Text("Item 3"),
          Text("Item 4"),
          Menu(
            Text("Nested", attributes = Set(Color("orange"))),
            Seq(
              Text("Item 5"),
              Text("Item 6"),
              ShellCommand(
                "Item 7",
                "echo",
                Seq("hello world"),
                terminal = true
              )
            )
          )
        )
      )
    )
  )
}
