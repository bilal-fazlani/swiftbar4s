package com.bilalfazlani.swiftbar4s.models
import scala.io.Source
import java.util.Base64
import scala.util.Try

sealed trait Image {
  def render: Option[String] = this match {
    case ResourceImage(path) =>
      Try {
        val bytes = Source
          .fromResource(path)
          .toArray
          .map(_.toByte)
        Base64.getEncoder.encodeToString(bytes)
      }.toOption
    case ImageSrc(url) =>
      Try {
        val bytes = Source
          .fromURL(url)
          .toArray
          .map(_.toByte)
        Base64.getEncoder.encodeToString(bytes)
      }.toOption
    case Base64Image(value) => Some(value)
  }
}

case class ResourceImage(path: String) extends Image
case class ImageSrc(url: String)       extends Image
case class Base64Image(value: String)  extends Image
