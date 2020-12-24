package com.bilalfazlani.swiftbar4s.models

sealed trait Tag(content: String, tagName: String) {
  override def toString() = s"# <$tagName>$content</$tagName>"
}

object Tag {
//About
  case class Title(value: String)   extends Tag(value, "bitbar.title")
  case class Version(value: String) extends Tag(value, "bitbar.version")
  case class Author(value: String)  extends Tag(value, "swiftbar.author")
  case class AuthorGithubUsername(value: String)
      extends Tag(value, "bitbar.author.github")
  case class Description(value: String) extends Tag(value, "bitbar.desc")
  case class ImageUrl(value: String)    extends Tag(value, "bitbar.image")
  case class Dependencies(values: String*)
      extends Tag(values.mkString(","), "bitbar.dependencies")
  case class AboutUrl(value: String) extends Tag(value, "bitbar.abouturl")

//Config
  case object HideAbout extends Tag("true", "swiftbar.hideAbout")
  case object HideRunInTerminal
      extends Tag("true", "swiftbar.hideRunInTerminal")
  case object HideLastUpdated extends Tag("true", "swiftbar.hideLastUpdated")
  case object HideDisablePlugin
      extends Tag("true", "swiftbar.hideDisablePlugin")
  case object HideSwiftBar extends Tag("true", "swiftbar.hideSwiftBar")
}
