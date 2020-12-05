package tech.bilal.bitbar4s.models

sealed trait MenuItem

object MenuItem {
  case class Text(
      text: String,
      attributes: Set[Attribute] = Set.empty
  ) extends MenuItem

  case class Link(
      text: String,
      url: String,
      attributes: Set[Attribute] = Set.empty
  ) extends MenuItem

  case class ShellCommand(
      text: String,
      executable: String,
      params: Seq[String] = Seq.empty,
      terminal: Boolean = false,
      refresh: Boolean = true,
      attributes: Set[Attribute] = Set.empty
  ) extends MenuItem

  case class Menu(
      text: Text,
      items: Seq[MenuItem]
  ) extends MenuItem
}
