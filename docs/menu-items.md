# Menu items

## Menu

Renders top level menu and menu items inside it. Also supports nested sub-menus.

## Text

Renders a non clickable static text menu item.

## Link

Renders a clickable link. Requires `url` parameter.

## Action

Renders an action item. A handler for this action must be present in the handlers.
Supports metadata, which will be passed to action handler.

## ShellCommand

Renders a shell command. Requires path of the command and its parameters.

## SubMenu

Renders a submenu with menu items. Supports all the common attributes.

## TopLevel

- Text
- Action
- ShellCommand
- Link

Renders a single item on the top level instead of a menu.  Supports all the common attributes.