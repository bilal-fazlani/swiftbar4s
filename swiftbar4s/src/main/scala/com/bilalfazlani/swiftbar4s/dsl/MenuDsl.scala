package com.bilalfazlani.swiftbar4s.dsl

import scala.collection.mutable.ListBuffer
import com.bilalfazlani.swiftbar4s.models.MenuItem
import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.MenuSubscriber
import com.bilalfazlani.swiftbar4s.dsl.SwiftBarRuntime
import com.bilalfazlani.swiftbar4s.models.Attribute
import com.bilalfazlani.swiftbar4s.models.Attribute.{Image as ImageAttribute, *}
import com.bilalfazlani.swiftbar4s.models.MenuItem.*
import com.bilalfazlani.swiftbar4s.utils.HttpClient

import scala.sys.env
import org.reactivestreams.{Processor, Publisher, Subscriber}
import com.bilalfazlani.swiftbar4s.models.SfScale
import com.bilalfazlani.swiftbar4s.models.SfWeight
import com.bilalfazlani.swiftbar4s.models.SfRenderingMode

type SimpleType = Text | Link | DispatchAction | ShellCommand

class MenuBuilder(textItem: Text) {
  var items: Seq[MenuItem] = Seq.empty
  add(textItem)
  def add(item: MenuItem) = {
    items = items.appended(item)
  }
  override def toString =
    items
      .map(_.toString)
      .mkString(s"MenuBuilder($textItem, Children(", ",", "))")

  def build: Menu = Menu(
    items.head.asInstanceOf,
    items
      .drop(1)
      .map {
        case x: Menu       => x
        case a: SimpleType => a
      }
      .toSeq
  )
}

type MenuFunction = MenuBuilder ?=> Unit

given Conversion[MenuBuilder, Menu] = _.build

enum Image:
  case Resource(path: String)
  case Url(url: String)
  case Base64(value: String)
  case None

trait MenuDsl extends Plugin {
  case object DefaultValue
  type ColorDsl     = String | DefaultValue.type
  type TextSizeDsl  = Int | DefaultValue.type
  type LengthDsl    = Int | DefaultValue.type
  type FontDsl      = String | DefaultValue.type
  type ToolTipDsl   = String | None.type
  type AlternateDsl = Boolean | DefaultValue.type
  type ShortcutDsl  = String | None.type
  type SFImageDsl   = SFImage | None.type

  enum Iconize {
    case Default
    case EmojiOnly
    case SFSymbolOnly
    case Disabled
  }

  enum SFImage:
    case Palette(
        name: String,
        primaryColor: String,
        secondaryColor: String,
        scale: SfScale = SfScale.Large,
        weight: SfWeight = SfWeight.Bold
    )
    case Hierarchical(
        name: String,
        color: String | DefaultValue.type = DefaultValue,
        scale: SfScale = SfScale.Large,
        weight: SfWeight = SfWeight.Bold
    )
    case Monochrome(
        name: String,
        color: String,
        scale: SfScale = SfScale.Large,
        weight: SfWeight = SfWeight.Bold
    )

  extension [T](value: T)
    infix def ifDark[A <: T](value2: A)(using Option[SwiftBarRuntime]): T =
      summon[Option[SwiftBarRuntime]] match {
        case Some(r) =>
          if r.osAppearance == OSAppearance.Light then value else value2
        case None => value
      }

  private def getUrlImage(url: String) = HttpClient
    .getBytes(url)
    .map(java.util.Base64.getEncoder.encodeToString)
    .getOrElse("")

  private def getResourceImage(path: String) = {
    val effectivePath = if path.startsWith("/") then path else ("/" + path)
    val img           = getClass.getResourceAsStream(effectivePath)
    if img == null then
      throw new RuntimeException(s"image $effectivePath not found in resources")
    val bytes = img.readAllBytes
    java.util.Base64.getEncoder.encodeToString(bytes)
  }

  private var topMenu: Option[MenuItem | Publisher[MenuItem]] = None

  def appMenu: MenuItem | Publisher[MenuItem] = topMenu match {
    case Some(mb: MenuItem)            => mb
    case Some(publisher: Publisher[?]) => publisher
    case None => throw new NotImplementedError("no menu")
  }

  def stream(publisher: Publisher[MenuItem]) = topMenu = Some(publisher)

  def menu(
      text: String,
      color: ColorDsl = DefaultValue,
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      sfImage: SFImageDsl = None,
      templateImage: Image = Image.None,
      ansi: Boolean = false,
      markdown: Boolean = false,
      iconize: Iconize = Iconize.Default,
      tooltip: ToolTipDsl = None,
      shortcut: ShortcutDsl = None
  )(init: MenuFunction): MenuBuilder = {
    given t: MenuBuilder = MenuBuilder(
      Text(
        text,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          false,
          ansi,
          markdown,
          iconize,
          tooltip,
          DefaultValue,
          shortcut
        )
      )
    )
    init
    topMenu = Some(t)
    t
  }

  def subMenu(
      text: String,
      color: ColorDsl = DefaultValue,
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      sfImage: SFImageDsl = None,
      templateImage: Image = Image.None,
      checked: Boolean = false,
      ansi: Boolean = false,
      markdown: Boolean = false,
      iconize: Iconize = Iconize.Default,
      tooltip: ToolTipDsl = None,
      shortcut: ShortcutDsl = None
  )(init: MenuFunction)(using menuDsl: MenuBuilder): MenuBuilder = {
    val innerMenu = MenuBuilder(
      Text(
        text,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          checked,
          ansi,
          markdown,
          iconize,
          tooltip,
          DefaultValue,
          shortcut
        )
      )
    )
    // format: off
    {
      given i: MenuBuilder = innerMenu
      init
    }
    summon[MenuBuilder].add(innerMenu)
    // format: on
    innerMenu
  }

  object topLevel {
    def sfSymbol(
        symbol: SFImage,
        tooltip: ToolTipDsl = None,
        shortcut: ShortcutDsl = None
    ): Text =
      val t = Text(
        "",
        getAttributes(
          DefaultValue,
          DefaultValue,
          DefaultValue,
          DefaultValue,
          Image.None,
          symbol,
          Image.None,
          false,
          false,
          false,
          Iconize.SFSymbolOnly,
          tooltip,
          DefaultValue,
          shortcut
        )
      )
      topMenu = Some(t)
      t

    def text(
        text: String,
        color: ColorDsl = DefaultValue,
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
        length: LengthDsl = DefaultValue,
        image: Image = Image.None,
        sfImage: SFImageDsl = None,
        templateImage: Image = Image.None,
        checked: Boolean = false,
        ansi: Boolean = false,
        markdown: Boolean = false,
        iconize: Iconize = Iconize.Default,
        tooltip: ToolTipDsl = None,
        shortcut: ShortcutDsl = None
    ): Text =
      val t = Text(
        text,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          checked,
          ansi,
          markdown,
          iconize,
          tooltip,
          DefaultValue,
          shortcut
        )
      )
      topMenu = Some(t)
      t

    def link(
        text: String,
        url: String,
        color: ColorDsl = DefaultValue,
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
        length: LengthDsl = DefaultValue,
        image: Image = Image.None,
        sfImage: SFImageDsl = None,
        templateImage: Image = Image.None,
        checked: Boolean = false,
        ansi: Boolean = false,
        markdown: Boolean = false,
        iconize: Iconize = Iconize.Default,
        tooltip: ToolTipDsl = None,
        alternate: AlternateDsl = DefaultValue,
        shortcut: ShortcutDsl = None
    ): Link =
      val l = Link(
        text,
        url,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          checked,
          ansi,
          markdown,
          iconize,
          tooltip,
          alternate,
          shortcut
        )
      )
      topMenu = Some(l)
      l

    def shellCommand(
        text: String,
        executable: String,
        showTerminal: Boolean = false,
        refresh: Boolean = true,
        color: ColorDsl = DefaultValue,
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
        length: LengthDsl = DefaultValue,
        image: Image = Image.None,
        sfImage: SFImageDsl = None,
        templateImage: Image = Image.None,
        checked: Boolean = false,
        ansi: Boolean = false,
        markdown: Boolean = false,
        iconize: Iconize = Iconize.Default,
        tooltip: ToolTipDsl = None,
        alternate: AlternateDsl = DefaultValue,
        shortcut: ShortcutDsl = None,
        params: String*
    ): ShellCommand =
      val s = ShellCommand(
        text,
        executable,
        params,
        showTerminal,
        refresh,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          checked,
          ansi,
          markdown,
          iconize,
          tooltip,
          alternate,
          shortcut
        )
      )
      topMenu = Some(s)
      s

    def actionDispatch(
        text: String,
        action: String,
        metadata: Option[String],
        showTerminal: Boolean = false,
        refresh: Boolean = true,
        color: ColorDsl = DefaultValue,
        textSize: TextSizeDsl = DefaultValue,
        font: FontDsl = DefaultValue,
        length: LengthDsl = DefaultValue,
        image: Image = Image.None,
        sfImage: SFImageDsl = None,
        templateImage: Image = Image.None,
        checked: Boolean = false,
        ansi: Boolean = false,
        markdown: Boolean = false,
        iconize: Iconize = Iconize.Default,
        tooltip: ToolTipDsl = None,
        alternate: AlternateDsl = DefaultValue,
        shortcut: ShortcutDsl = None
    ): DispatchAction =
      val d = DispatchAction(
        text,
        action,
        metadata,
        showTerminal,
        refresh,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          checked,
          ansi,
          markdown,
          iconize,
          tooltip,
          alternate,
          shortcut
        )
      )
      topMenu = Some(d)
      d
  }

  private def getAttributes(
      color: ColorDsl,
      textSize: TextSizeDsl,
      font: FontDsl,
      length: LengthDsl,
      image: Image,
      sfSymbol: SFImageDsl,
      templateImage: Image,
      checked: Boolean,
      ansi: Boolean,
      markdown: Boolean,
      iconize: Iconize,
      tooltip: ToolTipDsl,
      alternate: AlternateDsl,
      shortcut: ShortcutDsl
  ): Set[Attribute] = {
    var set = Set.empty[Attribute]
    if (color != DefaultValue) set = set + Color(color.asInstanceOf)
    if (textSize != DefaultValue) set = set + TextSize(textSize.asInstanceOf)
    if (font != DefaultValue) set = set + Font(font.asInstanceOf)
    if (length != DefaultValue) set = set + Length(length.asInstanceOf)
    image match {
      case Image.Base64(value) => set = set + ImageAttribute(value)
      case Image.Url(url)      => set = set + ImageAttribute(getUrlImage(url))
      case Image.Resource(path) =>
        set = set + ImageAttribute(getResourceImage(path))
      case Image.None =>
    }
    templateImage match {
      case Image.Base64(value) => set = set + TemplateImage(value)
      case Image.Url(url)      => set = set + TemplateImage(getUrlImage(url))
      case Image.Resource(path) =>
        set = set + TemplateImage(getResourceImage(path))
      case Image.None =>
    }
    if (checked) set = set + Checked(checked)
    if (ansi) set = set + ANSI(ansi)
    if (markdown) set = set + Markdown(markdown)
    if (tooltip != None) set = set + ToolTip(tooltip.asInstanceOf)
    if (alternate != DefaultValue) set = set + Alternate(alternate.asInstanceOf)
    if (shortcut != None) set = set + Shortcut(shortcut.asInstanceOf)

    sfSymbol match {
      case SFImage.Palette(name, primary, secondary, scale, weight) =>
        set ++= Seq(
          SfImage(name),
          SfConfig(
            SfRenderingMode.Palette,
            Seq(primary, secondary),
            scale,
            weight
          )
        )
      case SFImage.Hierarchical(name, color, scale, weight) =>
        val colors = color match
          case DefaultValue => Seq("primary")
          case str: String  => Seq(str)
        set ++= Seq(
          SfImage(name),
          SfConfig(SfRenderingMode.Hierarchical, colors, scale, weight)
        )
      case SFImage.Monochrome(name, color, scale, weight) =>
        set ++= Seq(
          SfImage(name),
          SfConfig(SfRenderingMode.Palette, Seq(color), scale, weight)
        )

      case None =>
    }

    iconize match {
      case Iconize.EmojiOnly =>
        set = set ++ Seq(Symbolize(false))
      case Iconize.SFSymbolOnly =>
        set = set ++ Seq(Emojize(false))
      case Iconize.Disabled =>
        set = set ++ Seq(Emojize(false), Symbolize(false))
      case Iconize.Default =>
    }
    set
  }

  def text(
      text: String,
      color: ColorDsl = DefaultValue,
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      sfImage: SFImageDsl = None,
      templateImage: Image = Image.None,
      checked: Boolean = false,
      ansi: Boolean = false,
      markdown: Boolean = false,
      iconize: Iconize = Iconize.Default,
      tooltip: ToolTipDsl = None,
      shortcut: ShortcutDsl = None
  ): MenuFunction = {
    summon[MenuBuilder].add(
      Text(
        text,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          checked,
          ansi,
          markdown,
          iconize,
          tooltip,
          DefaultValue,
          shortcut
        )
      )
    )
  }

  infix def --- : MenuFunction =
    summon[MenuBuilder].add(Text("---"))

  def link(
      text: String,
      url: String,
      color: ColorDsl = DefaultValue,
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      sfImage: SFImageDsl = None,
      templateImage: Image = Image.None,
      checked: Boolean = false,
      ansi: Boolean = false,
      markdown: Boolean = false,
      iconize: Iconize = Iconize.Default,
      tooltip: ToolTipDsl = None,
      alternate: AlternateDsl = DefaultValue,
      shortcut: ShortcutDsl = None
  ): MenuFunction = {
    summon[MenuBuilder].add(
      Link(
        text,
        url,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          checked,
          ansi,
          markdown,
          iconize,
          tooltip,
          alternate,
          shortcut
        )
      )
    )
  }

  def shellCommand(
      text: String,
      executable: String,
      showTerminal: Boolean = false,
      refresh: Boolean = true,
      color: ColorDsl = DefaultValue,
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      sfImage: SFImageDsl = None,
      templateImage: Image = Image.None,
      checked: Boolean = false,
      ansi: Boolean = false,
      markdown: Boolean = false,
      iconize: Iconize = Iconize.Default,
      tooltip: ToolTipDsl = None,
      alternate: AlternateDsl = DefaultValue,
      shortcut: ShortcutDsl = None,
      params: String*
  ): MenuFunction = {
    summon[MenuBuilder].add(
      ShellCommand(
        text,
        executable,
        params,
        showTerminal,
        refresh,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          checked,
          ansi,
          markdown,
          iconize,
          tooltip,
          alternate,
          shortcut
        )
      )
    )
  }

  def action(
      text: String,
      action: String,
      metadata: Option[String] = None,
      showTerminal: Boolean = false,
      refresh: Boolean = true,
      color: ColorDsl = DefaultValue,
      textSize: TextSizeDsl = DefaultValue,
      font: FontDsl = DefaultValue,
      length: LengthDsl = DefaultValue,
      image: Image = Image.None,
      sfImage: SFImageDsl = None,
      templateImage: Image = Image.None,
      checked: Boolean = false,
      ansi: Boolean = false,
      markdown: Boolean = false,
      iconize: Iconize = Iconize.Default,
      tooltip: ToolTipDsl = None,
      alternate: AlternateDsl = DefaultValue,
      shortcut: ShortcutDsl = None
  ): MenuFunction = {
    summon[MenuBuilder].add(
      DispatchAction(
        text,
        action,
        metadata,
        showTerminal,
        refresh,
        getAttributes(
          color,
          textSize,
          font,
          length,
          image,
          sfImage,
          templateImage,
          checked,
          ansi,
          markdown,
          iconize,
          tooltip,
          alternate,
          shortcut
        )
      )
    )
  }
}
