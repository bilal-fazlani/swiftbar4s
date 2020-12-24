package com.bilalfazlani.swiftbar4s.parser

case class Output(lines: Seq[String] = Seq.empty) {
  def append(value: String): Output = this.copy(lines.appended(value))
  def merge(out: Output): Output    = Output(lines ++ out.lines)
}
object Output {
  def apply(value: String): Output = Output(Seq(value))
}
