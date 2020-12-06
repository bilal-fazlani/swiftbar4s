val sv = "2.13.4"
//val sv = "3.0.0-M2"

inThisBuild(
  Seq(
    scalaVersion := sv,
    organization := "tech.bilal",
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
  .settings(
    name := "bitbar4s",
    libraryDependencies ++= Seq(
      Libs.munit % Test
    )
  )
