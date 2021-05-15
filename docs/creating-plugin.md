# Creating a plugin

A Swiftbar plugin is a menu which refreshes at given frequency.
The plugin appears in the top menu bar of macOS.

The menu can contain static text items, web links, shell commands, plugin actions items and sub menus. 
Each menu item support formatting options such as text color, text size, image, template image, length, trim, etc.

The only requirement to create a plugin is to extend from `PluginDsl`.

Following is an example of a plugin which has one top level item and two text items in the dropdown.

```scala
import com.bilalfazlani.swiftbar4s.dsl.*

object SimplePlugin extends PluginDsl {
  menu("my plugin") {
    text("item 1")
    text("item 2")
  }
}
```

!!! tip
    Most functionality of the framework is available by 
    importing `import com.bilalfazlani.swiftbar4s.dsl.*`

If you run this project using `sbt run`, it will print following content

```text
my plugin
---
item 1
item 2
```

SwiftBar parses this output and creates a menu as shown in the image

![simple](/images/creating-plugin/simple.png){: style="width:350px" loading=lazy }