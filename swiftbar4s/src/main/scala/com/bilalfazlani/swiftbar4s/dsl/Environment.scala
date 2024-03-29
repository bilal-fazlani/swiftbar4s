package com.bilalfazlani.swiftbar4s.dsl

import java.nio.file.Path

enum OSAppearance {
  case Light
  case Dark
}

enum OSVersion(val major: Int):
  case BigSur   extends OSVersion(11)
  case Monterey extends OSVersion(12)
  case Ventura  extends OSVersion(13)
  case Sonoma   extends OSVersion(14)

case class RetrievedOSVersion private[swiftbar4s] (
    major: Int,
    minor: Int,
    patch: Int
) {
  infix def is(version: OSVersion) =
    this.major == version.major
  infix def isNot(version: OSVersion) = !is(version)
  infix def <(version: OSVersion) =
    this.major < version.major
  infix def <=(version: OSVersion) = is(version) || <(version)
  infix def >(version: OSVersion)  = !(<=(version))
  infix def >=(version: OSVersion) = is(version) || >(version)

  override def toString = s"$major.$minor.$patch"
}

case class SwiftBarRuntime(
    swiftbarVersion: String,
    swiftbarBuild: String,
    pluginsPath: Path,
    selfPath: Path,
    cacheDir: Path,
    dataDir: Path,
    osAppearance: OSAppearance,
    osVersion: RetrievedOSVersion
) {
  lazy val pluginFileName: String =
    selfPath.getFileName.toString
}

trait Environment {
  private val get = (x => sys.env.get(x)).andThen(_.get)
  given runtime: Option[SwiftBarRuntime] = sys.env.get("SWIFTBAR") match {
    case Some("1") =>
      Some(
        SwiftBarRuntime(
          get("SWIFTBAR_VERSION"),
          get("SWIFTBAR_BUILD"),
          Path.of(get("SWIFTBAR_PLUGINS_PATH")),
          Path.of(get("SWIFTBAR_PLUGIN_PATH")),
          Path.of(get("SWIFTBAR_PLUGIN_CACHE_PATH")),
          Path.of(get("SWIFTBAR_PLUGIN_DATA_PATH")),
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
