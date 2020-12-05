package tech.bilal.bitbar4s.parser

import tech.bilal.bitbar4s.models.Attribute._
import tech.bilal.bitbar4s.models.{Attribute, MenuItem}
import tech.bilal.bitbar4s.models.MenuItem._

import scala.util.chaining._

class Parser {
  private val HEADER_SEPARATOR = "---"
  private val LEVEL_SEPARATOR  = "--"

  def parse(item: MenuItem): Output = parse(item, -1)

  private def render(
      value: String,
      level: Int,
      attributes: Set[Attribute]
  ): Output = {
    val separator = if (attributes.isEmpty) "" else " | "
    val suffix    = attributes.map(_.toString).mkString(" ")
    val l         = level.max(0)
    Output(s"${LEVEL_SEPARATOR * l}$value$separator$suffix")
  }

  private def parse(item: MenuItem, level: Int): Output = {
    item match {
      case Text(text, attributes) => render(text, level, attributes)
      case Link(text, url, attributes) =>
        render(s"$text | href=$url", level, attributes)
      case ShellCommand(
            text,
            executable,
            params,
            terminal,
            refresh,
            attributes
          ) =>
        val additionalAttributes = Seq(
          Some(Executable(executable)),
          if (params.nonEmpty) Some(Params(params)) else None,
          if (terminal) Some(Terminal) else None,
          if (refresh) Some(Refresh) else None
        ).flatten

        render(text, level, attributes ++ additionalAttributes)
      case Menu(text, items) =>
        render(text.text, level, text.attributes)
          .pipe(o => if (level == -1) o.append(HEADER_SEPARATOR) else o)
          .merge(
            items
              .map(i => parse(i, level + 1))
              .reduce(_ merge _)
          )
    }
  }
}
