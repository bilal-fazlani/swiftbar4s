package com.bilalfazlani.scalabar.parser

import com.bilalfazlani.scalabar.SelfPath
import com.bilalfazlani.scalabar.models.Attribute
import com.bilalfazlani.scalabar.models.Attribute._

class Renderer(selfPath: SelfPath) {
  private val LEVEL_SEPARATOR = "--"

  def renderLine(
      value: String,
      level: Int,
      attributes: Set[Attribute]
  ): Output = {
    val separator = if (attributes.isEmpty) "" else " | "
    val suffix    = attributes.map(renderAttribute).mkString(" ")
    Output(s"${LEVEL_SEPARATOR * level}$value$separator$suffix")
  }

  def renderAttribute(attribute: Attribute): String = {
    attribute match {
      case Font(name)           => s"""font="$name""""
      case Color(value)         => s"""color="$value""""
      case TextSize(value)      => s"size=$value"
      case TemplateImage(value) => s"templateImage=$value"
      case Image(value)         => s"image=$value"
      case Emojize(value)       => s"emojize=${value.toString.toLowerCase}"
      case Href(url)            => s"""href="$url""""
      case Executable("$0") =>
        s"""bash="${selfPath.get()}""""
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
}
