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

![color1](/images/customising-items/color-1.png){: style="width:300px" loading = lazy}

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

![tooltip](/images/customising-items/tooltip.png){: style="width:300px" loading = lazy}

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

![text-size](/images/customising-items/text-size.png){: style="width:380px" loading = lazy}

## Font

=== "Scala"

    ```scala
    menu("my plugin", font = "Noteworthy") {
      text("item")
      subMenu("abc", font = "Times", textSize = 25) {
        link("google", url = "https://google.com", font = "Impact", textSize = 25)
      }
    }
    ```

=== "Rendered"

    ```
    my plugin | font="Noteworthy"
    ---
    item
    abc | size=25 font="Times"
    --google | size=25 font="Impact" href="https://google.com"
    ```

`font` can take a String value representing font of the menu item text. You can also provide `DefaultValue` which will not render font attribute resulting into default behaviour of OS.

![font](/images/customising-items/font.png){: style="width:380px" loading = lazy}

## Length

=== "Scala"

    ```scala
    menu("my plugin") {
      text("adfx zczcxzc897 zxcxzcxz cqweas", length = 10)
      text("afkljkjk lajdklsajl kasjdlks ad")
      text("asdas 08as0d sd", length = DefaultValue)
      text("asxzc q4sd", length = 50)
      link("kljlasdasd 79asdxzca asd897asd", "google.com", length = 10)
    }
    ```

=== "Rendered"

    ```
    my plugin
    ---
    adfx zczcxzc897 zxcxzcxz cqweas | length=10
    afkljkjk lajdklsajl kasjdlks ad
    asdas 08as0d sd
    asxzc q4sd | length=50
    kljlasdasd 79asdxzca asd897asd | length=10 href="google.com"
    ```

`length` takes an Int value. If `length` is provided and item text length is greater than given length, it will trim the remaining characters and append `...`

You can also provide `DefaultValue` which will not render length attribute and text will not be trimmed.

![length](/images/customising-items/length.png){: style="width:380px" loading = lazy}

## Emojize



## Image

## TemplateImage

## Symbolize

## ANSI
**Conflicts with symbolize**


## SF Image

## SF Color

## Checked

## Shortcut

## Alternate