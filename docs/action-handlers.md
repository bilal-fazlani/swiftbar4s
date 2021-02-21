# Action handlers

When you create [action](/menu-items/#action) menu items, action handlers need to be created to handle the action invokations when user clicks on menu items.

You can have actions with or without metadata. Metadata is a string parameter which is passed to handler at the time of invokation.

!!! Info
    While rendering, action name and metadata are automatically encoded to base64. The framework decodes them back before invoking action handler.

For instance, in the below example, we have two actions defined - one with metadata and another without metadata. To create their handlers, we to first extend from `HandlerDsl` and then override `handler` method.

=== "Scala"

    ```scala
    import com.bilalfazlani.swiftbar4s.Plugin
    import com.bilalfazlani.swiftbar4s.dsl.HandlerDsl
    import com.bilalfazlani.swiftbar4s.dsl.MenuDsl

    object SimpleMenu extends Plugin with MenuDsl with HandlerDsl {
      
      override val handler = handler {
        handle("say-hello") {
          println("hello world")
        }

        handle("send-email") { emailMayBe =>
          if(emailMayBe.nonEmpty)
            sendEmail(emailMayBe.get) //sends email to john
        }
      }

      private def sendEmail(email: String) = ???

      override val appMenu = menu("my plugin") {
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
    If your business logic is async, for example, if the sendEmail method is async and returns a future, it is advised that you block it. This is perticularly useful when you want the plugin to refresh after handler invocation is finished.
