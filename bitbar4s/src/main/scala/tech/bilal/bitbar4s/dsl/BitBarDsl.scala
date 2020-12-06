package tech.bilal.bitbar4s.dsl

import tech.bilal.bitbar4s.models.Attribute.{
  Color,
  Emojize,
  Font,
  Image,
  Refresh,
  TemplateImage,
  Terminal,
  TextSize
}
import tech.bilal.bitbar4s.models.MenuItem._
import tech.bilal.bitbar4s.models.{Executable, MenuItem, WithAttribute}

trait BitBarDsl {
  def text(text: String): Text = Text(text)

  def link(text: String, url: String): Link = Link(text, url)

  def shellCommand(
      text: String,
      executablePath: String,
      params: String*
  ): ShellCommand =
    ShellCommand(text, executablePath, params)

  def dispatchAction(
      text: String,
      action: String
  ): DispatchAction = DispatchAction(text, action, None)

  def dispatchAction(
      text: String,
      action: String,
      metadata: String
  ): DispatchAction = DispatchAction(text, action, Some(metadata))

  implicit class AttrExtension[T <: WithAttribute[T]](item: WithAttribute[T]) {
    def textSize(value: Int): T =
      item.set(TextSize(value))

    def font(name: String): T =
      item.set(Font(name))

    def color(value: String): T =
      item.set(Color(value))

    def withImage(base64: String): T =
      item.set(Image(base64))

    def withTemplateImage(base64: String): T =
      item.set(TemplateImage(base64))

    def emojize(value: Boolean): T =
      item.set(Emojize(value))

    def emojize(): T =
      item.set(Emojize(true))
  }

  implicit class ExeExtension[T <: Executable[T]](item: Executable[T]) {
    def refresh(value: Boolean): T =
      item.set(Refresh(value))

    def refresh(): T =
      item.set(Refresh(true))

    def showTerminal(value: Boolean): T =
      item.set(Terminal(value))

    def showTerminal(): T =
      item.set(Terminal(true))
  }

  implicit class TextExtension(text: Text) {
    def >>(menuItem: MenuItem*): Menu =
      Menu(
        text,
        Seq(menuItem: _*)
      )
  }
}
