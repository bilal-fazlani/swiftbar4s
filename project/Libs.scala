import sbt._

object Libs {
  lazy val munit = "org.scalameta" %% "munit" % "0.7.25"
  lazy val `reactive-streams` =
    "org.reactivestreams" % "reactive-streams" % "1.0.3"
  lazy val `better-files` = ("com.github.pathikrit" %% "better-files" % "3.9.1")
    .cross(CrossVersion.for3Use2_13)
}

object NonProdLibs {
  lazy val `entities-client` =
    "com.github.bilal-fazlani.akka-http-streaming-events" % "streaming-client_2.13" % "main-SNAPSHOT"
}
