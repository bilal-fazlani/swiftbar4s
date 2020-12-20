package com.bilalfazlani.scalabar.models

import com.bilalfazlani.scalabar.models.Attribute._

import java.nio.file.Paths

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
        s"""bash="${Paths.get("").toAbsolutePath.toString}""""
      case Executable(path) => s"""bash="$path""""
      case Params(values) =>
        values.zipWithIndex
          .map { case (str, i) =>
            s"""param${i + 1}="$str""""
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
  private[scalabar] case class Href(url: String)           extends Attribute
  private[scalabar] case class Executable(path: String)    extends Attribute
  private[scalabar] case class Params(values: Seq[String]) extends Attribute
  private[scalabar] case class Refresh(enable: Boolean)    extends Attribute
  private[scalabar] case class Terminal(enable: Boolean)   extends Attribute
}
