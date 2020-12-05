package tech.bilal.bitbar4s.models

sealed trait ItemFormat

object ItemFormat {
  case class Font(name: String)           extends ItemFormat
  case class Color(value: String)         extends ItemFormat
  case class TextSize(value: Int)         extends ItemFormat
  case class TemplateImage(value: String) extends ItemFormat
  case class Image(value: String)         extends ItemFormat
}
