package com.bilalfazlani.swiftbar4s.parser

import com.bilalfazlani.swiftbar4s.models.Attribute
import com.bilalfazlani.swiftbar4s.models.Attribute._

class Renderer(selfPath: String) {
  private val LEVEL_SEPARATOR = "--"

  def renderLine(
      value: String,
      level: Int,
      attributes: Set[Attribute]
  ): Output = {
    val separator = if (attributes.isEmpty) "" else " | "
    val attrs    = attributes.map(renderAttribute).mkString(" ")
    Output(s"${LEVEL_SEPARATOR * level}$value$separator$attrs")
  }

  def renderAttribute(attribute: Attribute): String = {
    attribute match {
      case Font(name)           => s"""font="$name""""
      case Color(value)         => s"""color="$value""""
      case TextSize(value)      => s"size=$value"
      case Length(value)        => s"length=$value"
      case TemplateImage(value) => s"templateImage=$value"
      case Image(value)         => s"image=$value"
      case Emojize(value)       => s"emojize=${value.toString.toLowerCase}"
      case Symbolize(value)     => s"symbolize=${value.toString.toLowerCase}"
      case ToolTip(value)       => s"""tooltip="$value""""
      case Alternate(value)     => s"""alternate=$value"""
      case Href(url)            => s"""href="$url""""
      case Executable("$0") =>
        s"""bash="$selfPath""""
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
}
