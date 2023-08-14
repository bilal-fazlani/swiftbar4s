import sbt._

object Libs {
  lazy val `reactive-streams` =
    "org.reactivestreams" % "reactive-streams" % "1.0.4"
}

object NonProdLibs {
  lazy val munit = "org.scalameta" %% "munit" % "1.0.0-M8"
  // lazy val `entities-client` =
  //   "com.github.bilal-fazlani.akka-http-streaming-events" % "streaming-client_2.13" % "main-SNAPSHOT"
  lazy val rainbowcli = "com.github.bilal-fazlani" % "rainbowcli" % "2.0.1"

  val zioVersion = "2.0.15"

  lazy val zio     = "dev.zio" %% "zio"                         % zioVersion
  lazy val streams = "dev.zio" %% "zio-streams"                 % zioVersion
  lazy val interop = "dev.zio" %% "zio-interop-reactivestreams" % "2.0.2"
}
