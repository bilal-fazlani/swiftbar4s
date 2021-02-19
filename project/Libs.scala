import sbt._

object Libs {
  lazy val munit = "org.scalameta" %% "munit" % "0.7.22"
  lazy val `reactive-streams` =  "org.reactivestreams" % "reactive-streams" % "1.0.3"
}

object NonProdLibs {
  lazy val `entities-client` = "com.github.bilal-fazlani.akka-http-streaming-events" % "streaming-client_2.13" % "main-SNAPSHOT"
}
