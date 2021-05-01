package com.bilalfazlani.swiftbar4s.dsl

import scala.collection.mutable.ListBuffer
import com.bilalfazlani.swiftbar4s.models.MenuItem
import com.bilalfazlani.swiftbar4s.dsl.SwiftBarRuntime
import com.bilalfazlani.swiftbar4s.models.Attribute
import com.bilalfazlani.swiftbar4s.models.Attribute.*
import com.bilalfazlani.swiftbar4s.models.MenuItem.*
import scala.sys.env

extension [T] (value:T)
  infix def ifDark[A <: T](value2:A)(using Option[SwiftBarRuntime]): T = summon[Option[SwiftBarRuntime]] match {
    case Some(r) => if r.osAppearance == OSAppearance.Light then value else value2
    case None => value
  }

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

trait MenuDsl {
    case object DefaultValue
    type ColorDsl = String | DefaultValue.type
    type TextSizeDsl = Int | DefaultValue.type
    type LengthDsl = Int | DefaultValue.type
    type FontDsl = String | DefaultValue.type
    type ImageDsl = String | None.type
    type TemplateImageDsl = String | None.type
    type ToolTipDsl = String | None.type
    type AlternateDsl = Boolean | DefaultValue.type
    type ShortcutDsl = String | None.type

    enum Iconize {
      case Auto
      case EmojiOnly
      case SFSymbolOnly
      case Disabled
    }

    def menu(
      text:String,
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
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
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
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
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
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
        image: ImageDsl = None,
        templateImage: TemplateImageDsl = None,
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
        image: ImageDsl = None,
        templateImage: TemplateImageDsl = None,
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
        image: ImageDsl = None,
        templateImage: TemplateImageDsl = None,
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
      image: ImageDsl,
      templateImage: TemplateImageDsl,
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
        if(image != None) set = set + Image(image.asInstanceOf)
        if(templateImage != None) set = set + TemplateImage(templateImage.asInstanceOf)
        if(tooltip != None) set = set + ToolTip(tooltip.asInstanceOf)
        if(alternate != DefaultValue) set = set + Alternate(alternate.asInstanceOf)
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
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
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
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
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
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
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
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      iconize: Iconize = Iconize.Auto,
      tooltip:ToolTipDsl = None,
      alternate:AlternateDsl = DefaultValue,
      shortcut:ShortcutDsl = None,
      ): ContextFunction[MenuBuilder] = {
        summon[MenuBuilder].add(DispatchAction(text, action, metadata, showTerminal, refresh, 
          getAttributes(color, textSize, font, length, image, templateImage, iconize, tooltip, alternate, shortcut)))
    }
}