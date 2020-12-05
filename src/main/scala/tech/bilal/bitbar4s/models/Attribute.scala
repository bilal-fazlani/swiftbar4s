package tech.bilal.bitbar4s.models

import tech.bilal.bitbar4s.models.Attribute._

sealed trait Attribute {
  override def toString: String =
    this match {
      case Font(name)           => s"font=$name"
      case Color(value)         => s"color=$value"
      case TextSize(value)      => s"size=$value"
      case TemplateImage(value) => s"templateImage=$value"
      case Image(value)         => s"image=$value"
      case Emojize              => s"emojize=true"
      case Href(url)            => s"href=$url"
      case Executable(path)     => s"bash=$path"
      case Params(values) =>
        values.zipWithIndex
          .map {
            case (str, i) => s"param${i + 1}=$str"
          }
          .mkString(" | ")
      case Refresh  => s"refresh=true"
      case Terminal => s"terminal=true"
    }
}

object Attribute {
  case class Font(name: String)           extends Attribute
  case class Color(value: String)         extends Attribute
  case class TextSize(value: Int)         extends Attribute
  case class TemplateImage(value: String) extends Attribute
  case class Image(value: String)         extends Attribute
  case object Emojize                     extends Attribute
  //privates
  private[bitbar4s] case class Href(url: String)           extends Attribute
  private[bitbar4s] case class Executable(path: String)    extends Attribute
  private[bitbar4s] case class Params(values: Seq[String]) extends Attribute
  private[bitbar4s] case object Refresh                    extends Attribute
  private[bitbar4s] case object Terminal                   extends Attribute
}
