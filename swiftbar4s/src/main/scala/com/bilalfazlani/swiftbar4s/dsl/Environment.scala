package com.bilalfazlani.swiftbar4s.dsl

enum OSAppearance {
    case Light
    case Dark
}

case class OSVersion(major:Int, minor:Int) {
    override def toString = s"version: $major.$minor"
}

object OSVersion {
    lazy val Cheetah:OSVersion = OSVersion(10, 0)
    lazy val Puma:OSVersion = OSVersion(10, 1)
    lazy val Jaguar:OSVersion = OSVersion(10, 2)
    lazy val Panther:OSVersion = OSVersion(10, 3)
    lazy val Tiger:OSVersion = OSVersion(10, 4)
    lazy val Leopard:OSVersion = OSVersion(10, 5)
    lazy val SnowLeopard:OSVersion = OSVersion(10, 6)
    lazy val Lion:OSVersion = OSVersion(10, 7)
    lazy val MountainLion:OSVersion = OSVersion(10, 8)
    lazy val Mavericks:OSVersion = OSVersion(10, 9)
    lazy val Yosemite:OSVersion = OSVersion(10, 10)
    lazy val ElCapitan:OSVersion = OSVersion(10, 11)
    lazy val Sierra:OSVersion = OSVersion(10, 12)
    lazy val HighSierra:OSVersion = OSVersion(10, 13)
    lazy val Mojave:OSVersion = OSVersion(10, 14)
    lazy val Catalina:OSVersion = OSVersion(10, 15)
    lazy val BigSur:OSVersion = OSVersion(11, 0)
}

case class RetrievedOSVersion private[swiftbar4s](major:Int, minor:Int, patch:Int) {
    infix def is (version:OSVersion) = this.major == version.major && this.minor == version.minor
    infix def isNot (version:OSVersion) = !is(version)
    infix def < (version:OSVersion) = this.major < version.major || this.minor < version.minor
    infix def <= (version:OSVersion) = is(version) || <(version)
    infix def > (version:OSVersion) = !(<=(version))
    infix def >= (version:OSVersion) = is(version) || >(version)

    override def toString = s"$major.$minor.$patch"
}

case class SwiftBarRuntime(
    swiftbarVersion:String, 
    swiftbarBuild:String,
    pluginsPath:String,
    selfPath:String,
    osAppearance:OSAppearance,
    osVersion: RetrievedOSVersion
){
    lazy val pluginFileName:String = java.nio.file.Paths.get(selfPath).getFileName.toString
}

trait Environment {
    private val get = (x => sys.env.get(x)).andThen(_.get)
    given runtime:Option[SwiftBarRuntime] = sys.env.get("SWIFTBAR") match {
        case Some("1") => Some(SwiftBarRuntime(
            get("SWIFTBAR_VERSION"), 
            get("SWIFTBAR_BUILD"),
            get("SWIFTBAR_PLUGINS_PATH"),
            get("SWIFTBAR_PLUGIN_PATH"),
            if get("OS_APPEARANCE") == "Light" then OSAppearance.Light else OSAppearance.Dark,
            RetrievedOSVersion(get("OS_VERSION_MAJOR").toInt, get("OS_VERSION_MINOR").toInt, get("OS_VERSION_PATCH").toInt)
        ))
        case _ => None
    }
}