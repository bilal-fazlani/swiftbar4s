package com.bilalfazlani.swiftbar4s

import java.io.File
import scala.sys.process.Process

class SelfPath(pluginName: String) {

  def get(): String = {
    val output = (Process("defaults read com.matryer.BitBar") #| Process(
      "grep pluginsDirectory"
    )).lazyLines.head
    val pattern       = """\W*pluginsDirectory\W*=\W*\"(.*)\".*""".r
    val pattern(path) = output
    getListOfFiles(path)
      .find(isSelf)
      .map(_.toString)
      .getOrElse(throw new RuntimeException("could not find plugin file"))
  }

  private def isSelf(file: File): Boolean = {
    file.getName.startsWith(s"$pluginName.")
  }

  private def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
}
