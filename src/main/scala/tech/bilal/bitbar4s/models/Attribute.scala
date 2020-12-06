package tech.bilal.bitbar4s.models

import tech.bilal.bitbar4s.models.Attribute._

sealed trait Attribute {
  override def toString: String =
    this match {
      case Font(name)           => s"""font="$name""""
      case Color(value)         => s"""color="$value""""
      case TextSize(value)      => s"size=$value"
      case TemplateImage(value) => s"templateImage=$value"
      case Image(value)         => s"image=$value"
      case Emojize(value)       => s"emojize=${value.toString.toLowerCase}"
      case Href(url)            => s"""href="$url""""
      case Executable("$0") =>
        s"""bash="${getClass.getProtectionDomain.getCodeSource.getLocation}""""
      case Executable(path) => s"""bash="$path""""
      case Params(values) =>
        values.zipWithIndex
          .map {
            case (str, i) => s"""param${i + 1}="$str""""
          }
          .mkString(" ")
      case Refresh(enable)  => s"refresh=${enable.toString.toLowerCase}"
      case Terminal(enable) => s"terminal=${enable.toString.toLowerCase}"
    }
}

object Attribute {
  case class Font(name: String)           extends Attribute
  case class Color(value: String)         extends Attribute
  case class TextSize(value: Int)         extends Attribute
  case class TemplateImage(value: String) extends Attribute
  case class Image(value: String)         extends Attribute
  case class Emojize(value: Boolean)      extends Attribute
  //privates
  private[bitbar4s] case class Href(url: String)           extends Attribute
  private[bitbar4s] case class Executable(path: String)    extends Attribute
  private[bitbar4s] case class Params(values: Seq[String]) extends Attribute
  private[bitbar4s] case class Refresh(enable: Boolean)    extends Attribute
  private[bitbar4s] case class Terminal(enable: Boolean)   extends Attribute
}
