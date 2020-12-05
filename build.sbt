//val scalav = "2.13.4"
val scalav = "3.0.0-M2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "bitbar4s",
    version := "0.1.0",
    scalaVersion := scalav
  )
