package com.bilalfazlani.swiftbar4s.dsl

import scala.collection.mutable.ListBuffer
import com.bilalfazlani.swiftbar4s.models.MenuItem
import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.MenuSubscriber
import com.bilalfazlani.swiftbar4s.dsl.SwiftBarRuntime
import com.bilalfazlani.swiftbar4s.models.Attribute
import com.bilalfazlani.swiftbar4s.models.Attribute.{Image as ImageAttribute, *}
import com.bilalfazlani.swiftbar4s.models.MenuItem.*
import scala.sys.env
import org.reactivestreams.{Publisher, Processor, Subscriber}

type AllowedType = Text | Link | DispatchAction | ShellCommand | MenuBuilder

class MenuBuilder(val textItem:Text) {
  val items:ListBuffer[AllowedType] = new ListBuffer()
  def add(item:AllowedType) = {
    items.addOne(item)
  }

  override def toString = items.map(_.toString).mkString(s"MenuDsl($textItem, Children(", ",", "))")

  type SimpleType = Text | Link | DispatchAction | ShellCommand

  def build: Menu = Menu(textItem, items.map{
    case x:MenuBuilder => x.build
    case a:SimpleType  => a
  }.toSeq)
}

type ContextFunction[T] = T ?=> Unit

enum Image:
  case Resource(path:String)
  // case Url(url:String)
  case Base64(value:String)
  case None

trait MenuDsl extends Plugin {
    case object DefaultValue
    type ColorDsl = String | DefaultValue.type
    type TextSizeDsl = Int | DefaultValue.type
    type LengthDsl = Int | DefaultValue.type
    type FontDsl = String | DefaultValue.type
    type ToolTipDsl = String | None.type
    type AlternateDsl = Boolean | DefaultValue.type
    type ShortcutDsl = String | None.type

    enum Iconize {
      case Auto
      case EmojiOnly
      case SFSymbolOnly
      case Disabled
    }

    extension [T] (value:T)
      infix def ifDark[A <: T](value2:A)(using Option[SwiftBarRuntime]): T = summon[Option[SwiftBarRuntime]] match {
        case Some(r) => if r.osAppearance == OSAppearance.Light then value else value2
        case None => value
      }

    private def getResourceImage(path:String) = {
      val effectivePath = if path.startsWith("/") then path else ("/" + path)
      val img = getClass.getResourceAsStream(effectivePath)
      if img == null then throw new RuntimeException(s"image $effectivePath not found in resources")
      val bytes  = img.readAllBytes
      java.util.Base64.getEncoder.encodeToString(bytes)
    }

    def menu : MenuBuilder | Publisher[MenuBuilder]

    def appMenu: Menu | Publisher[Menu] = menu match {
      case mb: MenuBuilder => mb.build
      case _ => throw new NotImplementedError("Publisher[Menu] is not supported yet")
    }

    def menu(
      text:String,
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      templateImage: Image = Image.None,
      iconize: Iconize = Iconize.Auto,
      tooltip:ToolTipDsl = None,
      shortcut:ShortcutDsl = None,
      )(init: ContextFunction[MenuBuilder]): MenuBuilder = {
      given t:MenuBuilder = MenuBuilder(Text(text, 
        getAttributes(color, textSize, font, length, image, templateImage, iconize, tooltip, DefaultValue, shortcut)))
      init
      t
    }

    def subMenu(
      text:String, 
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      templateImage: Image = Image.None,
      iconize: Iconize = Iconize.Auto,
      tooltip:ToolTipDsl = None,
      shortcut:ShortcutDsl = None,
      )(init: ContextFunction[MenuBuilder])(using menuDsl:MenuBuilder): MenuBuilder = {
      val innerMenu = MenuBuilder(Text(text, 
        getAttributes(color, textSize, font, length, image, templateImage, iconize, tooltip, DefaultValue, shortcut)))
      summon[MenuBuilder].add(innerMenu)
      {
        given i:MenuBuilder = innerMenu
        init
      }
      innerMenu
    }

    object topLevel {
      def text(
      text:String, 
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      templateImage: Image = Image.None,
      iconize: Iconize = Iconize.Auto,
      tooltip:ToolTipDsl = None,
      shortcut:ShortcutDsl = None,
      ): Text = Text(text, 
        getAttributes(color, textSize, font, length, image, templateImage, iconize,tooltip, DefaultValue, shortcut))
    
      def link(
        text:String, 
        url:String,
        color:ColorDsl = DefaultValue, 
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
        length: LengthDsl = DefaultValue,
        image: Image = Image.None,
        templateImage: Image = Image.None,
        iconize: Iconize = Iconize.Auto,
        tooltip:ToolTipDsl = None,
        alternate:AlternateDsl = DefaultValue,
        shortcut:ShortcutDsl = None,
        ): Link = Link(text, url, 
          getAttributes(color, textSize, font, length, image, templateImage, iconize,tooltip, alternate, shortcut))
      
      def shellCommand(
        text:String, 
        executable:String,
        showTerminal:Boolean = false,
        refresh:Boolean = true,
        color:ColorDsl = DefaultValue, 
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
        length: LengthDsl = DefaultValue,
        image: Image = Image.None,
        templateImage: Image = Image.None,
        iconize: Iconize = Iconize.Auto,
        tooltip:ToolTipDsl = None,
        alternate:AlternateDsl = DefaultValue,
        shortcut:ShortcutDsl = None,
        params:String*,
        ): ShellCommand  = ShellCommand(text, executable, params, showTerminal, refresh, 
          getAttributes(color, textSize, font, length, image, templateImage, iconize, tooltip, alternate, shortcut))
      

      def actionDispatch(
        text:String, 
        action: String,
        metadata:Option[String],
        showTerminal:Boolean = false,
        refresh:Boolean = true,
        color:ColorDsl = DefaultValue, 
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
        length: LengthDsl = DefaultValue,
        image: Image = Image.None,
        templateImage: Image = Image.None,
        iconize: Iconize = Iconize.Auto,
        tooltip:ToolTipDsl = None,
        alternate:AlternateDsl = DefaultValue,
        shortcut:ShortcutDsl = None,
        ): DispatchAction = DispatchAction(text,action, metadata, showTerminal, refresh, 
          getAttributes(color, textSize, font, length, image, templateImage, iconize, tooltip, alternate, shortcut))
    }

    private def getAttributes(
      color:ColorDsl,
      textSize: TextSizeDsl,
      font: FontDsl,
      length: LengthDsl,
      image: Image,
      templateImage: Image,
      iconize: Iconize,
      tooltip:ToolTipDsl,
      alternate:AlternateDsl,
      shortcut:ShortcutDsl,
    ):Set[Attribute] = {
        var set = Set.empty[Attribute]
        if(color != DefaultValue) set = set + Color(color.asInstanceOf)
        if(textSize != DefaultValue) set = set + TextSize(textSize.asInstanceOf)
        if(font != DefaultValue) set = set + Font(font.asInstanceOf)
        if(length != DefaultValue) set = set + Length(length.asInstanceOf)
        image match {
          case Image.Base64(value) => set = set + ImageAttribute(value)
          // case Image.Url(url) => ???
          case Image.Resource(path) => set = set + ImageAttribute(getResourceImage(path))
          case Image.None => 
        }
        templateImage match {
          case Image.Base64(value) => set = set + TemplateImage(value)
          // case Image.Url(url) => ???
          case Image.Resource(path) => set = set + TemplateImage(getResourceImage(path))
          case Image.None => 
        }
        if(tooltip != None) set = set + ToolTip(tooltip.asInstanceOf)
        if(alternate != DefaultValue) set = set + Alternate(alternate.asInstanceOf)
        if(shortcut != None) set = set + Shortcut(shortcut.asInstanceOf)
        iconize match {
          case Iconize.EmojiOnly => 
            set = set ++ Seq(Symbolize(false))
          case Iconize.SFSymbolOnly =>
            set = set ++ Seq(Emojize(false))
          case Iconize.Disabled =>
            set = set ++ Seq(Emojize(false), Symbolize(false))
          case Iconize.Auto => 
        }
        set
    }

    def text(
      text:String, 
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      templateImage: Image = Image.None,
      iconize: Iconize = Iconize.Auto,
      tooltip:ToolTipDsl = None,
      shortcut:ShortcutDsl = None,
      ): ContextFunction[MenuBuilder] = {
        summon[MenuBuilder].add(Text(text, 
        getAttributes(color, textSize, font, length, image, templateImage, iconize, tooltip, DefaultValue, shortcut)))
    }

    infix def --- : ContextFunction[MenuBuilder] = summon[MenuBuilder].add(Text("---"))

    def fromResource(fileName:String) = ???
    def fromUrl(fileName:String) = ???

    def link(
      text:String, 
      url:String,
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      templateImage: Image = Image.None,
      iconize: Iconize = Iconize.Auto,
      tooltip:ToolTipDsl = None,
      alternate:AlternateDsl = DefaultValue,
      shortcut:ShortcutDsl = None,
      ): ContextFunction[MenuBuilder] = {
        summon[MenuBuilder].add(Link(text, url, 
          getAttributes(color, textSize, font, length, image, templateImage, iconize, tooltip, alternate, shortcut)))
    }

    def shellCommand(
      text:String, 
      executable:String,
      showTerminal:Boolean = false,
      refresh:Boolean = true,
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      templateImage: Image = Image.None,
      iconize: Iconize = Iconize.Auto,
      tooltip:ToolTipDsl = None,
      alternate:AlternateDsl = DefaultValue,
      shortcut:ShortcutDsl = None,
      params:String*,
      ): ContextFunction[MenuBuilder] = {
        summon[MenuBuilder].add(ShellCommand(text, executable, params, showTerminal, refresh, 
          getAttributes(color, textSize, font, length, image, templateImage, iconize, tooltip, alternate, shortcut)))
    }

    def action(
      text:String, 
      action: String,
      metadata:Option[String] = None,
      showTerminal:Boolean = false,
      refresh:Boolean = true,
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      templateImage: Image = Image.None,
      iconize: Iconize = Iconize.Auto,
      tooltip:ToolTipDsl = None,
      alternate:AlternateDsl = DefaultValue,
      shortcut:ShortcutDsl = None,
      ): ContextFunction[MenuBuilder] = {
        summon[MenuBuilder].add(DispatchAction(text, action, metadata, showTerminal, refresh, 
          getAttributes(color, textSize, font, length, image, templateImage, iconize, tooltip, alternate, shortcut)))
    }
}