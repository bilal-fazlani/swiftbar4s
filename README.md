# SwiftBar4s

SwiftBar4s enables scala developers to create plugins for [swiftbar](https://github.com/swiftbar/SwiftBar) in a declarative and easy way

![Sonatype Nexus (Releases)](https://img.shields.io/nexus/r/com.bilal-fazlani/swiftbar4s_3.0.0-M3?color=green&label=RELEASE%20VERSION&server=https%3A%2F%2Foss.sonatype.org&style=for-the-badge)

![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/com.bilal-fazlani/swiftbar4s_3.0.0-M3?label=SNAPSHOT%20VERSION&server=https%3A%2F%2Foss.sonatype.org&style=for-the-badge)
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
  override val appMenu = menu("my-plugin", color = if(isDarkMode) "white" else "red", textSize = 20) {
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

![demo](docs/images/demo.png)

You can create, static texts, web links, shell command triggers, and most importantly, actions.

All the items support configuratios such as color, text size, image, emojis etc. 

Tips:

- Use `isDarkMode` to change colors or images based on system theme.
- Use `---` to add line separators
- Use `isSwiftBar` to know whther plugin was called by SwiftBar
