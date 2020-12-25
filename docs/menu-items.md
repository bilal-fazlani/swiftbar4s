# Menu items

## Menu

Renders top level menu and allows other menu items inside it.

example of empty menu:

=== "Scala"

    ```scala
    override val appMenu = menu("my plugin") {

    }
    ```
    
=== "Rendered"
    
    ```text
    my plugin
    ---
    ```

example with menu items:

=== "Scala"

    ```scala
    override val appMenu = menu("my plugin") {
      text("item 1")
      link("click me", url="https://something.com")
    }
    ```

=== "Rendered"

    ```
    my plugin
    ---
    item 1
    click me | href=https://something.com
    ```

## Text

Renders a non clickable text menu item.

=== "Scala"

    ```scala
    menu("my plugin") {
      text("item 1")
    }
    ```

=== "Rendered"

    ```text
    my plugin
    ---
    item 1
    ```

## Link

Renders a clickable link and opens given url.

=== "Scala"

    ```scala
    override val appMenu = menu("my plugin") {
      link("click me", url="https://something.com")
    }
    ```

=== "Rendered"

    ```
    my plugin
    ---
    click me | href=https://something.com
    ```

**Additional Parameters**

| Param | Type   | Required | Default | Description     |
|-------|--------|----------|---------|-----------------|
| `url` | String | Y        |         | Url of the link |

## ShellCommand

Renders a shell command. Requires path of the command and its parameters(optional).

=== "Scala"

    ```scala
    menu("my plugin") {
      shellCommand("i will print hello on terminal", executable = "echo", showTerminal=true, params = "hello")
    }
    ```

=== "Rendered"

    ```
    my plugin
    ---
    i will print hello on terminal | bash="echo" param1="hello" terminal=true refresh=true
    ```

**Additional Parameters**

| Param          | Type              | Required | Default | Description                                  |
|----------------|-------------------|----------|---------|----------------------------------------------|
| `executable`   | String            | Y        |         | Command to be executed                       |
| `showTerminal` | Boolean           | N        | False   | Opens terminal and show command output       |
| `refresh`      | Boolean           | N        | True    | Refreshes the plugin after command execution |
| `params`       | String* (varargs) | N        |         | Parameters to command                        |


## Action

Renders an action item. A handler for this action must be defined in the plugin.
Supports metadata, which will be passed to action handler method.

=== "Scala"

    ```scala
    menu("my plugin") {
      action("send notification", action = "send-notification")
      action("send notification to john", action = "send-notification-to", metadata = Some("john"))
    }
    ```

=== "Rendered"

    ```
    my plugin
    ---
    send notification | bash="<SWIFTBAR_PLUGIN_PATH>" param1="dispatch" param2="c2VuZC1ub3RpZmljYXRpb24=" terminal=false refresh=true
    send notification to john | bash="<SWIFTBAR_PLUGIN_PATH>" param1="dispatch" param2="c2VuZC1ub3RpZmljYXRpb24tdG8=" param3="am9obg==" terminal=false refresh=true
    ```

**Additional Parameters**

| Param          | Type    | Required | Default | Description                                  |
|----------------|---------|----------|---------|----------------------------------------------|
| `action`       | String  | Y        |         | Name of the action to invoke                 |
| `metadata`     | String  | N        | None    | Data to be passed to action handler          |
| `showTerminal` | Boolean | N        | False   | Opens terminal and show command output       |
| `refresh`      | Boolean | N        | True    | Refreshes the plugin after command execution |


## SubMenu

Renders a sub-menu with nested menu items. Supports all menu items which are supported in root menu, including another subMenu.

=== "Scala"

    ```scala
    menu("my plugin") {
        text("top level")
        subMenu("another menu") {
          text("nested item")
          subMenu("one more"){
            link("google", "google.com")
            link("bing", "bing.com")
          }
        }
    }
    ```

=== "Rendered"

    ```
    my plugin
    ---
    top level
    another menu
    --nested item
    --one more
    ----google | href=google.com
    ----bing | href=bing.com
    ```