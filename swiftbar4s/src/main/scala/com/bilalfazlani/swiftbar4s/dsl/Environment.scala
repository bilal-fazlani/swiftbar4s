package com.bilalfazlani.swiftbar4s.dsl

enum OSAppearance {
  case Light
  case Dark
}

enum OSVersion(val major: Int, val minors: Seq[Int]):
  case Catalina extends OSVersion(10, Seq(15))
  case BigSur extends OSVersion(11, Seq(0, 1, 2))

case class RetrievedOSVersion private[swiftbar4s] (
    major: Int,
    minor: Int,
    patch: Int
) {
  infix def is(version: OSVersion) =
    this.major == version.major && version.minors.contains(this.minor)
  infix def isNot(version: OSVersion) = !is(version)
  infix def <(version: OSVersion) =
    this.major < version.major || (this.major == version.major && this.minor < version.minors.min)
  infix def <=(version: OSVersion) = is(version) || <(version)
  infix def >(version: OSVersion)  = !(<=(version))
  infix def >=(version: OSVersion) = is(version) || >(version)

  override def toString = s"$major.$minor.$patch"
}

case class SwiftBarRuntime(
    swiftbarVersion: String,
    swiftbarBuild: String,
    pluginsPath: String,
    selfPath: String,
    osAppearance: OSAppearance,
    osVersion: RetrievedOSVersion
) {
  lazy val pluginFileName: String =
    java.nio.file.Paths.get(selfPath).getFileName.toString
}

trait Environment {
  private val get = (x => sys.env.get(x)).andThen(_.get)
  given runtime: Option[SwiftBarRuntime] = sys.env.get("SWIFTBAR") match {
    case Some("1") =>
      Some(
        SwiftBarRuntime(
          get("SWIFTBAR_VERSION"),
          get("SWIFTBAR_BUILD"),
          get("SWIFTBAR_PLUGINS_PATH"),
          get("SWIFTBAR_PLUGIN_PATH"),
          if get("OS_APPEARANCE") == "Light" then OSAppearance.Light
          else OSAppearance.Dark,
          RetrievedOSVersion(
            get("OS_VERSION_MAJOR").toInt,
            get("OS_VERSION_MINOR").toInt,
            get("OS_VERSION_PATCH").toInt
          )
        )
      )
    case _ => None
  }
}
