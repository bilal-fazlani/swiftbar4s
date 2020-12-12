package com.bilalfazlani.scalabar.dsl

import scala.collection.mutable.ListBuffer
import com.bilalfazlani.scalabar.models.MenuItem
import com.bilalfazlani.scalabar.models.Attribute
import com.bilalfazlani.scalabar.models.Attribute._
import com.bilalfazlani.scalabar.models.MenuItem._
import scala.sys.env

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
    type FontDsl = String | DefaultValue.type
    type ImageDsl = String | None.type
    type TemplateImageDsl = String | None.type
    type EmojizeDsl = Boolean | DefaultValue.type

    def menu(
      text:String, 
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue
      )(init: ContextFunction[MenuBuilder]): MenuBuilder = {
      given t as MenuBuilder(Text(text, getAttributes(color, textSize, font, image, templateImage, emojize)))
      init
      t
    }

    def subMenu(
      text:String, 
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue
      )(init: ContextFunction[MenuBuilder])(using menuDsl:MenuBuilder): MenuBuilder = {
      val innerMenu = MenuBuilder(Text(text, getAttributes(color, textSize, font, image, templateImage, emojize)))
      summon[MenuBuilder].add(innerMenu)
      {
        given i as MenuBuilder = innerMenu
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
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue
      ): Text = Text(text, getAttributes(color, textSize, font, image, templateImage, emojize))
    
      def link(
        text:String, 
        url:String,
        color:ColorDsl = DefaultValue, 
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue
        ): Link = Link(text, url, getAttributes(color, textSize, font, image, templateImage, emojize))
      
      def shellCommand(
        text:String, 
        executable:String,
        showTerminal:Boolean = false,
        refresh:Boolean = true,
        color:ColorDsl = DefaultValue, 
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue,
        params:String*,
        ): ShellCommand  = ShellCommand(text, executable, params, showTerminal, refresh, getAttributes(color, textSize, font, image, templateImage, emojize))
      

      def actionDispatch(
        text:String, 
        action: String,
        metadata:Option[String],
        showTerminal:Boolean = false,
        refresh:Boolean = true,
        color:ColorDsl = DefaultValue, 
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue
        ): DispatchAction = DispatchAction(text,action, metadata, showTerminal, refresh, getAttributes(color, textSize, font, image, templateImage, emojize))
    }

    private def getAttributes(
      color:ColorDsl,
      textSize: TextSizeDsl,
      font: FontDsl,
      image: ImageDsl,
      templateImage: TemplateImageDsl,
      emojize: EmojizeDsl
    ):Set[Attribute] = {
        var set = Set.empty[Attribute]
        if(color != DefaultValue) set = set + Color(color.asInstanceOf)
        if(textSize != DefaultValue) set = set + TextSize(textSize.asInstanceOf)
        if(font != DefaultValue) set = set + Font(font.asInstanceOf)
        if(image != None) set = set + Image(image.asInstanceOf)
        if(templateImage != None) set = set + TemplateImage(templateImage.asInstanceOf)
        if(emojize != DefaultValue) set = set + Emojize(emojize.asInstanceOf)
        set
    }

    def text(
      text:String, 
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue
      ): ContextFunction[MenuBuilder] = {
        summon[MenuBuilder].add(Text(text, getAttributes(color, textSize, font, image, templateImage, emojize)))
    }

    def --- : ContextFunction[MenuBuilder] = summon[MenuBuilder].add(Text("---"))

    def isBitBar = env.get("BitBar").getOrElse("0") == "1"
    def isDarkMode = env.get("BitBarDarkMode").getOrElse("0") == "1"

    def link(
      text:String, 
      url:String,
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue
      ): ContextFunction[MenuBuilder] = {
        summon[MenuBuilder].add(Link(text, url, getAttributes(color, textSize, font, image, templateImage, emojize)))
    }

    def shellCommand(
      text:String, 
      executable:String,
      showTerminal:Boolean = false,
      refresh:Boolean = true,
      color:ColorDsl = DefaultValue, 
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue,
      params:String*,
      ): ContextFunction[MenuBuilder] = {
        summon[MenuBuilder].add(ShellCommand(text, executable, params, showTerminal, refresh, getAttributes(color, textSize, font, image, templateImage, emojize)))
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
      image: ImageDsl = None,
      templateImage: TemplateImageDsl = None,
      emojize: EmojizeDsl = DefaultValue
      ): ContextFunction[MenuBuilder] = {
        summon[MenuBuilder].add(DispatchAction(text,action, metadata, showTerminal, refresh, getAttributes(color, textSize, font, image, templateImage, emojize)))
    }
}