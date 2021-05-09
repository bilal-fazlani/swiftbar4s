<a href="https://swiftbar4s.bilal-fazlani.com">
  <img src="docs/images/logo-svg.svg" width=200 />
</a>

# SwiftBar4s is a scala framework to create [swiftbar](https://github.com/swiftbar/SwiftBar) plugins for MacOS

![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/com.bilal-fazlani/swiftbar4s_3.0.0-RC3?color=green&label=RELEASE%20VERSION&server=https%3A%2F%2Foss.sonatype.org&style=for-the-badge)

![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.bilal-fazlani/swiftbar4s_3.0.0-RC3?label=SNAPSHOT%20VERSION&server=https%3A%2F%2Foss.sonatype.org&style=for-the-badge)

# Documentation

## https://swiftbar4s.bilal-fazlani.com

# Usage

```scala
import com.bilalfazlani.swiftbar4s.dsl.*

object SimplePlugin extends PluginDsl {
  handler {
    handle("send-email") { emailMayBe =>
      println(s"email sent to $emailMayBe")
    }

    handle("print-hello") {
      println("hello world")
    }
  }

  menu("my-plugin", color = "red,green") {
    action("send email", "send-email", Some("abc@xyz.com"), true)
    action("print hello", "print-hello", showTerminal = true)
    text("item 1", font = "Times")
    ---

    text("item 2", textSize = 15)
    subMenu("submenu"){
      text("item 3", length = 4)
      text("item 4")
      Range(5,10).foreach{ i =>
        link(s"item $i", "http://google.com")
      }
      subMenu("nested", color = "orange"){
        text("item 10")
        ---

        text("item 11")
        shellCommand("item 12", "echo", showTerminal = true ,params = "hello world", "sds")
      }
    }
  }
}
```

<img src="docs/images/demo.png" width=400 />

You can create text, web links, shell commands and plugin actions.

All the items support configurations such as color, text size, image, emojis etc.
