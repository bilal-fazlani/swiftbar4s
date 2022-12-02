import sbt._

object Libs {
  lazy val `reactive-streams` =
    "org.reactivestreams" % "reactive-streams" % "1.0.4"
}

object NonProdLibs {
  lazy val munit = "org.scalameta" %% "munit" % "0.7.29"
  lazy val `entities-client` =
    "com.github.bilal-fazlani.akka-http-streaming-events" % "streaming-client_2.13" % "main-SNAPSHOT"
  lazy val rainbowcli = "com.github.bilal-fazlani" % "rainbowcli" % "2.0.1"
}
