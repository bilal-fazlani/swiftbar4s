package tech.bilal.bitbar4s.models

sealed trait MenuItem
sealed trait WithAttribute[T] extends MenuItem {
  val attributes: Set[Attribute]
  def set(attribute: Attribute): T
}

sealed trait Executable[T] extends MenuItem {
  val terminal: Boolean
  val refresh: Boolean
  val attributes: Set[Attribute]
  def set(attribute: Attribute): T
}

object MenuItem {
  case class Text(
      text: String,
      attributes: Set[Attribute] = Set.empty
  ) extends WithAttribute[Text] {
    override def set(attribute: Attribute): Text =
      copy(attributes = attributes + attribute)
  }

  case class Link(
      text: String,
      url: String,
      attributes: Set[Attribute] = Set.empty
  ) extends WithAttribute[Link] {
    override def set(attribute: Attribute): Link =
      copy(attributes = attributes + attribute)
  }

  case class ShellCommand(
      text: String,
      executable: String,
      params: Seq[String] = Seq.empty,
      terminal: Boolean = false,
      refresh: Boolean = true,
      attributes: Set[Attribute] = Set.empty
  ) extends WithAttribute[ShellCommand]
      with Executable[ShellCommand] {
    override def set(attribute: Attribute): ShellCommand =
      copy(attributes = attributes + attribute)
  }

  case class DispatchAction(
      text: String,
      action: String,
      metadata: Option[String],
      terminal: Boolean = false,
      refresh: Boolean = true,
      attributes: Set[Attribute] = Set.empty
  ) extends WithAttribute[DispatchAction]
      with Executable[DispatchAction] {
    override def set(attribute: Attribute): DispatchAction =
      copy(attributes = attributes + attribute)
  }

  case class Menu(
      text: Text,
      items: Seq[MenuItem]
  ) extends MenuItem
}
