# Creating a plugin

A swiftbar plugin is a menu which refreshes at given frequency.
The plugin appears in the top menu bar of Mac OS.

The menu can contain static text items, web links, shell commands, plugin actions items and sub menus. Each menu item support formatting options such as text color, text size, image, template image, length, trim, etc.

The bare minimum requirement to create a plugin is to extend from `SwiftBarPlugin` and `MenuDsl` and override `appMenu`.

Following is an example of a plugin which has one top level item and two text items in the dropdown.

```scala
import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.dsl.MenuDsl

object SimplePlugin extends Plugin with MenuDsl {
  override val appMenu = menu("my plugin") {
    text("item 1")
    text("item 2")
  }
}
```

If you run this project using `sbt run`, it will print following content

```text
my plugin
---
item 1
item 2
```

SwiftBar parses this output and creates a menu as shown in the image

![simple](/images/creating-plugin/simple.png){: style="width:350px" loading=lazy }