//val sv = "2.13.5"
val sv = "3.0.0-RC3"

val org = "com.bilal-fazlani"

inThisBuild(
  Seq(
    scalaVersion := sv,
    organization := org,
    homepage := Some(url("https://github.com/bilal-fazlani/swiftbar4s")),
    licenses := List(
      "MIT" -> url(
        "https://github.com/bilal-fazlani/swiftbar4s/blob/main/LICENSE"
      )
    ),
    developers := List(
      Developer(
        "bilal-fazlani",
        "Bilal Fazlani",
        "bilal.m.fazlani@gmail.com",
        url("https://bilal-fazlani.com")
      )
    ),
    resolvers += Resolver.JCenterRepository,
    scalacOptions ++= Seq(
      "-source", "future",
      "-deprecation"
    )
  )
)

lazy val root = project
  .in(file("."))
  .aggregate(swiftbar4s, myplugin, `swiftbar4s-devtools`)
  .settings(
    publish / skip  := true
  )

lazy val swiftbar4s = project
  .in(file("./swiftbar4s"))
  .settings(
    name := "swiftbar4s",
    libraryDependencies ++= Seq(
      Libs.`reactive-streams`,
      Libs.munit % Test
    )
  )

lazy val `swiftbar4s-devtools` = project
  .in(file("./swiftbar4s-devtools"))
  .settings(
    name := "swiftbar4s-devtools",
    libraryDependencies ++= Seq(
      Libs.`better-files`
    )
  )
  .dependsOn(swiftbar4s)

lazy val myplugin = project
  .in(file("./myplugin"))
  .settings(
    name := "myplugin",
    resolvers += "jitpack" at "https://jitpack.io",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      NonProdLibs.`entities-client`
    ),
    publish / skip  := !(sys.env
      .getOrElse("PUBLISH_PLUGIN", "false") == "true")
  )
  .dependsOn(`swiftbar4s-devtools`)
