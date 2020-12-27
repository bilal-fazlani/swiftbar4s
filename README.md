# SwiftBar4s is a scala framework to create [swiftbar](https://github.com/swiftbar/SwiftBar) plugins for MacOS

![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/com.bilal-fazlani/swiftbar4s_3.0.0-M3?color=green&label=RELEASE%20VERSION&server=https%3A%2F%2Foss.sonatype.org&style=for-the-badge)

![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.bilal-fazlani/swiftbar4s_3.0.0-M3?label=SNAPSHOT%20VERSION&server=https%3A%2F%2Foss.sonatype.org&style=for-the-badge)

## Documentation

**https://swiftbar4s.bilal-fazlani.com**

<a href="https://swiftbar4s.bilal-fazlani.com">
  <img src="docs/images/documentation-preview.png" width=350 />
</a>

## Usage

- create an `object` and extend from `SwiftBarApp with MenuDsl with HandlerDsl`

- create a handler for actions your plugin may dispatch. Example:

```scala
  override val handler = handler {
    handle("send-email") { emailMayBe =>
      emailMayBe.map(sendEmail)
    }

    handle("print-hello") {
      println("hello world")
    }
  }
```

- Use the dsl to create menu items either statically for dynamically. Example:

```scala
  override val appMenu = menu("my-plugin", color = "red,white", textSize = 20) {
    action("send email", "send-email", Some("abc@xyz.com"), true)
    action("print hello", "print-hello", showTerminal = true)
    text("item 1", font = "Times")
    ---
    text("item 2", textSize = 15)
    subMenu("submenu"){
      text("item 3")
      text("item 4")
      Range(20,30).foreach{ i =>
        link(s"item_$i", "http://google.com")
      }
      subMenu("nested", color = "orange"){
        text("item 5")
        ---
        text("item 6")
        shellCommand("item 7", "echo", showTerminal = true ,params = "hello world", "sds")
      }
    }
  }
```

<img src="docs/images/demo.png" width=400 />

You can create text, web links, shell commands, and plugin actions.

All the items support configuratios such as color, text size, image, emojis etc.
