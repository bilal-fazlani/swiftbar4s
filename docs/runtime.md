# Runtime

You are provided with a runtime api which provide basic information about the runtime environment

Runtime object gives you the following information

| API             | Description                            |
|-----------------|----------------------------------------|
| `swiftbarVersion` | SwiftBar version number                |
| `swiftbarBuild`   | SwiftBar build number                  |
| `pluginsPath`     | SwiftBar plugins directory path        |
| `selfPath`        | Path of the plugin file being executed |
| `osAppearance`    | Enum - will be either Light or Dark    |
| `osVersion`       | MacOS version                          |

!!! Warning "Important"
    Runtime information will only be available when plugin is executed via SwiftBar

=== "Scala"

    ```scala
    object MyPlugin extends PluginDsl {
      menu("my-plugin") {
        runtime match {
            case None => 
              text("Not running via swiftbar")
            case Some(r) if r.osVersion is OSVersion.BigSur =>
              text("this plugin does not work with BigSur")
            case Some(r) => 
              text(s"OS version: ${r.osVersion}")
              text(s"OS theme: ${r.osAppearance}")
              text(s"Plugin file: ${r.pluginFileName}")
        }
      }
    }
    ```

=== "Rendered"

    ```
    my-plugin
    ---
    OS version: 10.15.7
    OS theme: Dark
    Plugin file: myplugin.10m.bin
    ```

![simple](/images/runtime/rendered.png){: style="width:350px" loading=lazy }    

Depending on runtime info, plugin will be rendered differently

=== "Scenario 1"

    ```
    my-plugin
    ---
    Not running via swiftbar
    ```

=== "Scenario 2"

    ```
    my-plugin
    ---
    this plugin does not work with BigSur
    ```

## OS Version

There are some helpful functions available over `runtime.osVersion`

```scala
def is (version:OSVersion):Boolean
def isNot (version:OSVersion):Boolean
def < (version:OSVersion):Boolean
def <= (version:OSVersion):Boolean
def > (version:OSVersion):Boolean
def >= (version:OSVersion):Boolean
```

For example,

```scala
osVersion <= OSVersion.Catalina
osVersion > OSVersion.BigSur
osVersion isNot OSVersion.Catalina
osVersion is OSVersion.Catalina
```
