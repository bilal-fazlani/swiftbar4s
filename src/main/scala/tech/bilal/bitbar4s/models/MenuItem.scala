package tech.bilal.bitbar4s.models

sealed trait MenuItem

object MenuItem {
  case class Text(
      text: String,
      emojize: Boolean = true
  ) extends MenuItem

  case class Link(
      text: String,
      url: String,
      emojize: Boolean = true,
      refresh: Boolean = true
  ) extends MenuItem

  case class ShellCommand(
      text: String,
      script: String,
      params: Seq[String] = Seq.empty,
      terminal: Boolean = false,
      emojize: Boolean = true,
      refresh: Boolean = true
  ) extends MenuItem

  case class Menu(
      text: Text,
      items: Seq[MenuItem]
  ) extends MenuItem
}
