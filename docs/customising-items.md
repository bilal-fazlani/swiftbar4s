# Customising items

All menu items support customisations for below parameters. All of these customisations are optional.

## Color

=== "Scala"

    ```scala
    override val appMenu = menu("my plugin", color = "yellow") {
      text("menu item 1", color = "red,pink")
      text("menu item 2", color = "#11bf40")
      text("menu item 3", color = DefaultValue)
      text("menu item 4")
    }
    ```

=== "Rendered"

    ```
    my plugin | color="yellow"
    ---
    menu item 1 | color="red,pink"
    menu item 2 | color="#11bf40"
    menu item 3
    menu item 4
    ```

`color` can take a string value representing a single color or two comma separated colors.
In case two colors are provided, first will be used for light theme and second for dark theme.
Instead of a string, you can also provide `DefaultValue` which will have no impact on color and render default colors.

![color1](/images/customising-items/color-1.png){: style="width:300px"}

<div>
  <img src="/images/customising-items/color-2.png" style="height:300px" />
  <img src="/images/customising-items/color-3.png" style="height:300px" />
</div>

## Tooltip

=== "Scala"

    ```scala
    menu("my plugin") {
      text("menu item 1", tooltip = "hello")
    }
    ```

=== "Rendered"

    ```
    my plugin
    ---
    menu item 1 | tooltip="hello"
    ```

Renders tooltips when mouse over for few seconds

![tooltip](/images/customising-items/tooltip.png){: style="width:300px"}

## Text Size

=== "Scala"

    ```scala
    menu("my plugin") {
      text("item", textSize = 40)
      subMenu("abc", textSize = DefaultValue) {
        link("google", url = "https://google.com", textSize = 30)
      }
    }
    ```

=== "Rendered"

    ```
    my plugin
    ---
    item | size=40
    abc
    --google | size=30 href="https://google.com"
    ```

`textSize` can take an Int value representing font size of the menu item. You can also provide `DefaultValue` which will not render size attribute resulting into default behaviour of OS.

![text-size](/images/customising-items/text-size.png){: style="width:380px"}

## Font



## Image
## TemplateImage
## Emojize
## Symbolize
## Length
## SF Image
## Checked
## Shortcut