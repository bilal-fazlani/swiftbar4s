package com.bilalfazlani.swiftbar4s.parser

import com.bilalfazlani.swiftbar4s.models.Attribute
import com.bilalfazlani.swiftbar4s.models.Attribute.*
import java.util.Base64

class Renderer(selfPath: String) {
  private val LEVEL_SEPARATOR = "--"

  def renderLine(
      value: String,
      level: Int,
      attributes: Set[Attribute]
  ): Output = {
    val separator = if (attributes.isEmpty) "" else " | "
    val attrs     = attributes.map(renderAttribute).mkString(" ")
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
      case SfImage(name)        => s"sfimage=$name"
      case SfConfig(mode, colors, scale, weight) => {
        val colorsArray =
          s"""["${colors.map(_.toLowerCase).mkString("\",\"")}"]"""
        val json =
          s"""{"renderingMode":"$mode", "colors":$colorsArray, "scale": "${scale.toString.toLowerCase}", "weight": "${weight.toString.toLowerCase}"}"""
        val base64    = Base64.getEncoder().encode(json.getBytes("UTF-8"))
        val configStr = new String(base64, "UTF-8")
        s"""sfconfig="$configStr""""
      }
      case SfColor(name)    => s"sfcolor=$name"
      case SfSize(name)     => s"sfsize=$name"
      case Emojize(value)   => s"emojize=${value.toString.toLowerCase}"
      case Symbolize(value) => s"symbolize=${value.toString.toLowerCase}"
      case ToolTip(value)   => s"""tooltip="$value""""
      case Alternate(value) => s"""alternate=$value"""
      case Shortcut(value)  => s"""shortcut=${value.replace(" ", "")}"""
      case Checked(value)   => s"checked=$value"
      case ANSI(value)      => s"ansi=$value"
      case Markdown(value)  => s"md=$value"
      case Href(url)        => s"""href="$url""""
      case Executable("$0") => s"""bash="$selfPath""""
      case Executable(path) => s"""bash="$path""""
      case Refresh(enable)  => s"refresh=${enable.toString.toLowerCase}"
      case Terminal(enable) => s"terminal=${enable.toString.toLowerCase}"
      case Params(values) =>
        values.zipWithIndex
          .map { case (str, i) =>
            s"""param${i + 1}="$str""""
          }
          .mkString(" ")
    }
  }
}
