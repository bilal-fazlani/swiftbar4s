# Action handlers

When you create [action](/menu-items/#action) menu items, action handlers need to be created to handle the action 
invocations when user clicks on menu items.

You can have actions with or without metadata. Metadata is a string parameter which is passed to handler at the time of 
invocation.

!!! Info
    While rendering, action name and metadata are automatically encoded to base64. The framework decodes them back 
before invoking action handler.

For instance, in the below example, we have two actions defined - one with metadata and another without metadata. 
To create their handlers, we need to use the `handler` api. Within then handler, we get access to `handle` api which can
used multiple times to define a separate handler for each action.

=== "Scala"

    ```scala
    import com.bilalfazlani.swiftbar4s.dsl.*

    object SimpleMenu extends PluginDsl {
      
      handler {
        handle("say-hello") {
          println("hello world")
        }

        handle("send-email") { emailMayBe =>
          if(emailMayBe.nonEmpty)
            sendEmail(emailMayBe.get) //sends email to john
        }
      }

      private def sendEmail(email: String) = ???

      menu("my plugin") {
          action("say hello", action = "say-hello", showTerminal=true)
          action("send email", action = "send-email", metadata = Some("john@google.com"))
        }
    }
    ```

=== "Rendered"

    ```
    my plugin
    ---
    say hello | bash="<SWIFTBAR_PLUGIN_PATH>" param1="dispatch" param2="c2F5LWhlbGxv" terminal=true refresh=true
    send email | bash="<SWIFTBAR_PLUGIN_PATH>" param1="dispatch" param2="c2VuZC1lbWFpbA==" param3="am9obkBnb29nbGUuY29t" terminal=false refresh=true
    ```

The menu looks like this

![menu](/images/action-handlers/simple.png){: style="width:300px" loading=lazy}

!!! Attention "Important"
    If your business logic is async, for example, if the sendEmail method is async and returns a future, it is advised 
that you block it. This is particularly useful when you want the plugin to refresh after handler invocation is finished.
