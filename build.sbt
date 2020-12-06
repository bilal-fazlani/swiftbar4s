//val sv = "2.13.4"
val sv = "3.0.0-M2"

val org = "tech.bilal"

inThisBuild(
  Seq(
    scalaVersion := sv,
    organization := org,
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
  .aggregate(bitbar4s, myplugin)
  .settings(
    skip in publish := true
  )

lazy val bitbar4s = project
  .in(file("./bitbar4s"))
  .settings(
    name := "bitbar4s",
    libraryDependencies ++= Seq(
      Libs.munit % Test
    )
  )

lazy val myplugin = project
  .in(file("./myplugin"))
  .settings(
    name := "myplugin",
    skip in publish := !(sys.env
      .getOrElse("PUBLISH_PLUGIN", "false") == "true")
  )
  .dependsOn(bitbar4s)
