package tech.bilal.bitbar4s.parser

import tech.bilal.bitbar4s.models.{Attribute, MenuItem}
import tech.bilal.bitbar4s.models.MenuItem._

import scala.util.chaining._

class Parser {
  private val HEADER_SEPARATOR = "---"
  private val LEVEL_SEPARATOR  = "--"

  def parse(menu: Menu): Output = {
    render(menu.text.text, 0, menu.text.attributes)
      .pipe(_.append(HEADER_SEPARATOR))
      .merge(
        menu.items
          .map(i => parse(i, 0))
          .reduce(_ merge _)
      )
  }

  private def render(
      value: String,
      level: Int,
      attributes: Set[Attribute]
  ): Output = {
    val separator = if (attributes.isEmpty) "" else " | "
    val suffix    = attributes.map(_.toString).mkString(" ")
    Output(s"${LEVEL_SEPARATOR * level}$value$separator$suffix")
  }

  private def parse(item: MenuItem, level: Int): Output = {
    item match {
      case Text(text, attributes) => render(text, level, attributes)
      case Link(text, url, attributes) =>
        render(s"$text | href=$url", level, attributes)
      case ShellCommand(
            text,
            script,
            params,
            terminal,
            refresh,
            attributes
          ) =>
        render(text, level, attributes)
      case Menu(text, items) =>
        render(text.text, level, text.attributes)
          .merge(
            items
              .map(i => parse(i, level + 1))
              .reduce(_ merge _)
          )
    }
  }
}
