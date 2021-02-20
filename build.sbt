//val sv = "2.13.4"
val sv = "3.0.0-M3"

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
    testFrameworks += TestFramework("munit.Framework")
  )
)

lazy val root = project
  .in(file("."))
  .aggregate(swiftbar4s, myplugin)
  .settings(
    skip in publish := true
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

lazy val myplugin = project
  .in(file("./myplugin"))
  .settings(
    name := "myplugin",
    resolvers += "jitpack" at "https://jitpack.io",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      NonProdLibs.`entities-client`
    ),
    skip in publish := !(sys.env
      .getOrElse("PUBLISH_PLUGIN", "false") == "true")
  )
  .dependsOn(swiftbar4s)
