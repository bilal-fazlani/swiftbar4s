val org = "com.bilal-fazlani"

inThisBuild(
  Seq(
    scalaVersion  := "3.3.0",
    organization  := org,
    versionScheme := Some("semver-spec"),
    homepage      := Some(url("https://github.com/bilal-fazlani/swiftbar4s")),
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
    )
  )
)

lazy val swiftbar4s_root = project
  .in(file("."))
  .aggregate(swiftbar4s, myplugin)
  .settings(
    publish / skip := true
  )

lazy val swiftbar4s = project
  .in(file("./swiftbar4s"))
  .settings(
    name := "swiftbar4s",
    libraryDependencies ++= Seq(
      Libs.`reactive-streams`,
      NonProdLibs.munit % Test
    )
  )

lazy val myplugin = project
  .in(file("./myplugin"))
  .settings(
    name := "myplugin",
    resolvers += "jitpack" at "https://jitpack.io",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      // NonProdLibs.`entities-client`,
      NonProdLibs.rainbowcli,
      NonProdLibs.zio,
      NonProdLibs.streams,
      NonProdLibs.interop
    ),
    publish / skip := !(sys.env
      .getOrElse("PUBLISH_PLUGIN", "false") == "true")
  )
  .dependsOn(`swiftbar4s`)
