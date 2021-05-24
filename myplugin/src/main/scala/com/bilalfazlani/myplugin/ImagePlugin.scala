package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.dsl.*

object ImagePlugin extends PluginDsl {
   menu("Jobs", image = Image.Resource("/plugin-icon.png")){
     text("Job 1 success", image = Image.Resource("/success.png"))
     text("Job 2 failed", image = Image.Resource("/failed.png"))
     text("", image = Image.Url("https://miro.medium.com/max/1268/1*t07Qg1oeVKneuX6UDp-SvQ.jpeg"))
   }
}
