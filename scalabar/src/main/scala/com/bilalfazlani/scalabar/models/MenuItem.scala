package com.bilalfazlani.scalabar.models

sealed trait MenuItem
// sealed trait WithAttribute[T] extends MenuItem {
//   val attributes: Set[Attribute]
//   def set(attribute: Attribute): T
// }

// sealed trait Executable[T] extends MenuItem {
//   val terminal: Boolean
//   val refresh: Boolean
//   val attributes: Set[Attribute]
//   def set(attribute: Attribute): T
// }

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

  case class DispatchAction(
      text: String,
      action: String,
      metadata: Option[String],
      terminal: Boolean = false,
      refresh: Boolean = true,
      attributes: Set[Attribute] = Set.empty
  ) extends MenuItem

  case class Menu(
      text: Text,
      items: Seq[MenuItem]
  ) extends MenuItem
}
