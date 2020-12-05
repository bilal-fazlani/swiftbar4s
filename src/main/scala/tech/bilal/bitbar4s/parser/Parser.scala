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
      .add(
        menu.items
          .map(i => parse(i, 0, Output()))
          .reduce(_ add _)
      )
  }

  private def render(value: String, level: Int, output: Output): Output =
    output.append(s"${LEVEL_SEPARATOR * level}$value")

  private def parse(item: MenuItem, level: Int, output: Output): Output = {
    item match {
      case Text(text, emojize) => render(text, level, output)
      case Link(text, url, emojize, refresh) =>
        render(s"$text | href=$url", level, output)
      case ShellCommand(text, script, params, terminal, emojize, refresh) =>
        render(text, level, output)
      case Menu(text, items) =>
        render(text.text, level, output)
          .add(
            items
              .map(i => parse(i, level + 1, Output()))
              .reduce(_ add _)
          )
    }
  }
}
