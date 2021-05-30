package com.bilalfazlani.swiftbar4s.models

import com.bilalfazlani.swiftbar4s.models.Attribute.*

sealed trait Attribute

object Attribute {
  case class Font(name: String)           extends Attribute
  case class Color(value: String)         extends Attribute
  case class TextSize(value: Int)         extends Attribute
  case class TemplateImage(value: String) extends Attribute
  case class Image(value: String)         extends Attribute
  case class Emojize(value: Boolean)      extends Attribute
  case class Symbolize(value: Boolean)    extends Attribute
  case class ToolTip(value: String)       extends Attribute
  case class Alternate(value: Boolean)    extends Attribute
  case class Length(value: Int)           extends Attribute
  case class Shortcut(value: String)      extends Attribute
  case class Checked(value: Boolean)      extends Attribute
  case class ANSI(value: Boolean)         extends Attribute

  //privates
  private[swiftbar4s] case class Href(url: String)           extends Attribute
  private[swiftbar4s] case class Executable(path: String)    extends Attribute
  private[swiftbar4s] case class Params(values: Seq[String]) extends Attribute
  private[swiftbar4s] case class Refresh(enable: Boolean)    extends Attribute
  private[swiftbar4s] case class Terminal(enable: Boolean)   extends Attribute
}
