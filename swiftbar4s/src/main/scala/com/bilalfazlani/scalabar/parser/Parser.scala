package com.bilalfazlani.swiftbar4s.parser

import com.bilalfazlani.swiftbar4s.models.Attribute._
import com.bilalfazlani.swiftbar4s.models.MenuItem
import com.bilalfazlani.swiftbar4s.models.MenuItem._

import java.util.Base64
import scala.util.chaining._

class Parser(renderer: Renderer) {
  private val HEADER_SEPARATOR = "---"

  def parse(item: MenuItem): Output = parse(item, -1)

  private def parse(item: MenuItem, level: Int): Output = {
    item match {
      case Text(text, attributes) =>
        renderer.renderLine(text, level, attributes)
      case Link(text, url, attributes) =>
        renderer.renderLine(s"$text | href=$url", level, attributes)
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
        renderer.renderLine(text, level, attributes ++ additionalAttributes)

      case DispatchAction(
            text,
            action,
            metadata,
            terminal,
            refresh,
            attributes
          ) =>
        def encode(str: String): String =
          Base64.getEncoder.encodeToString(str.getBytes)
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
        renderer.renderLine(text, level, attributes ++ additionalAttributes)
      case Menu(text, items) =>
        renderer
          .renderLine(text.text, level, text.attributes)
          .pipe(o => if (level == -1) o.append(HEADER_SEPARATOR) else o)
          .merge(
            items
              .map(i => parse(i, level + 1))
              .foldRight(Output())(_ merge _)
          )
    }
  }
}
