package tech.bilal.bitbar4s.parser

import tech.bilal.bitbar4s.models.MenuItem
import tech.bilal.bitbar4s.models.MenuItem._

import scala.util.chaining._

class Parser {
  private val HEADER_SEPARATOR = "---"
  private val LEVEL_SEPARATOR  = "--"

  def parse(menu: Menu): Output = {
    Output(menu.text.text)
      .pipe(_.append(HEADER_SEPARATOR))
      .merge(
        menu.items
          .map(i => parse(i, 0))
          .reduce(_ merge _)
      )
  }

  private def render(value: String, level: Int): Output =
    Output(s"${LEVEL_SEPARATOR * level}$value")

  private def parse(item: MenuItem, level: Int): Output = {
    item match {
      case Text(text, emojize) => render(text, level)
      case Link(text, url, emojize, refresh) =>
        render(s"$text | href=$url", level)
      case ShellCommand(text, script, params, terminal, emojize, refresh) =>
        render(text, level)
      case Menu(text, items) =>
        render(text.text, level)
          .merge(
            items
              .map(i => parse(i, level + 1))
              .reduce(_ merge _)
          )
    }
  }
}
