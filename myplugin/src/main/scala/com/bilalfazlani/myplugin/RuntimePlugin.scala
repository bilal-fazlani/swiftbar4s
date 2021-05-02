package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.*
import com.bilalfazlani.swiftbar4s.dsl.*
import com.bilalfazlani.swiftbar4s.dsl.HandlerDsl
import com.bilalfazlani.swiftbar4s.dsl.MenuDsl

object RuntimePlugin extends MenuDsl {
  menu("my-plugin", shortcut = "ABC") {
    runtime match {
        case None => 
          text("Not running via swiftbar")
        case Some(r) => 
          import r.*
          
          osVersion <= OSVersion.Catalina
          osVersion > OSVersion.BigSur
          osVersion isNot OSVersion.Catalina
          osVersion is OSVersion.Catalina
          
          text(s"OS version: ${r.osVersion}")
          text(s"OS theme: ${r.osAppearance}")
          text(s"Plugin file: ${r.pluginFileName}")
    }
  }
}
