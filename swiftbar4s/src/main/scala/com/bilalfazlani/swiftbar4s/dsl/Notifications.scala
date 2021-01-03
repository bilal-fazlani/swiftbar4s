package com.bilalfazlani.swiftbar4s.dsl

import scala.sys.process._

trait Notifications { this:Environment =>
    def notify(title:String, subtitle:Option[String] = None, body:Option[String] = None):Unit = {
        runtime.foreach{ r => 
            val baseUrl = s"swiftbar://notify?plugin=${r.pluginFileName}&title=$title"
            val sub = subtitle.map(x => s"&subtitle=$x").getOrElse("")
            val b = body.map(x => s"&body=$x").getOrElse("")
            val url = baseUrl + sub + b
            s"""open "$url"""".!!
        }
    }
}