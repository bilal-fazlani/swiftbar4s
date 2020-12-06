package tech.bilal.bitbar4s.parser

import tech.bilal.bitbar4s.models.Attribute._
import tech.bilal.bitbar4s.models.{Attribute, MenuItem}
import tech.bilal.bitbar4s.models.MenuItem._

import java.util.Base64
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
          Executable(executable),
          Params(params),
          Terminal(terminal),
          Refresh(refresh)
        )
        render(text, level, attributes ++ additionalAttributes)

      case DispatchAction(
            text,
            action,
            metadata,
            terminal,
            refresh,
            attributes
          ) =>
        def encode(str: String) = Base64.getEncoder.encodeToString(str.getBytes)
        val additionalAttributes = Seq(
          Executable("$0"),
          Params(
            Seq(
              Some("dispatch"),
              Some(encode(action)),
              metadata.map(encode)
            ).flatten
          ),
          Terminal(terminal),
          Refresh(refresh)
        )
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
