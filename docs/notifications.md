# Notifications

Swiftbar supports notifications. If you want to trigger a notification, there is a `notify` api available.


=== "Scala"

    ```scala
    object MyPlugin extends PluginDsl {
        val data = Service.fetchData

        if(data.exists(_.isNew)) 
            notify(s"new data is available!")

        menu("my-plugin") {
            data.foreach(record => 
                text(record.description)
            )
        }
    }

    case class Record(isNew:Boolean, id:Int, description:String)

    //Fake service
    object Service {
        def fetchData:List[Record] = List(
            Record(isNew = false, id = 1, description = "A"),
            Record(isNew = false, id = 2, description = "B"),
            Record(isNew = true, id = 3, description = "C"),
        )
    }
    ```

=== "Rendered"

    ```
    my-plugin
    ---
    A
    B
    C
    ```

Notice that notifications have no impact on what is rendered. You can use `notify` api anywhere, for example: at the start of program, while rendering, or even from action handlers. In the above example, notification will get triggered after program starts and data is fetched but before menu is rendered.

Notification api supports title, subtitle and body

```scala
def notify(
      title: String,
      subtitle: Option[String] = None,
      body: Option[String] = None
  ): Unit
```

!!! Warning "Remember"
    For notifications to work, it is required that plugin is triggered via Swiftbar and not manually