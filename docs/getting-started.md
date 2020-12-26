# Getting started

![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/com.bilal-fazlani/swiftbar4s_3.0.0-M3?color=green&label=RELEASE%20VERSION&server=https%3A%2F%2Foss.sonatype.org&style=for-the-badge)

![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.bilal-fazlani/swiftbar4s_3.0.0-M3?label=SNAPSHOT%20VERSION&server=https%3A%2F%2Foss.sonatype.org&style=for-the-badge)

You can either use giter8 template to setup the project or setup the projet manually

## Using Giter8

If giter8 is not installed, it can be installed using coursier.

!!! Tip "Tip"
    Coursier CLI can be installed using instructions given on [official coursier website](https://get-coursier.io/docs/cli-installation)

The following command installs giter8

```bash
cs install giter8
```

Now, lets bootstrap a swiftbar plugin project

```bash
g8 bilal-fazlani/swiftbar4s.g8
```

## Manual setup

Add the following sbt dependency

```scala
libraryDependencies += "com.bilal-fazlani" %% "swiftbar4s" % "<VERSION>"
```
Since project is created using scala 3, you will need `sbt-dotty` plugin. Add the following line in `project/plugins.sbt`

```
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.5.1")
```

Set the scalaVersion to `3.0.0-M3`

```scala
inThisBuild(
  Seq(scalaVersion := "3.0.0-M3")
)
```



