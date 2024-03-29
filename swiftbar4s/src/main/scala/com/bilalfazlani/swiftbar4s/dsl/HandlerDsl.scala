package com.bilalfazlani.swiftbar4s.dsl

import scala.collection.mutable.ListBuffer
import com.bilalfazlani.swiftbar4s.models.MenuItem
import com.bilalfazlani.swiftbar4s.Handler
import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.models.Attribute
import com.bilalfazlani.swiftbar4s.models.Attribute.*
import com.bilalfazlani.swiftbar4s.models.MenuItem.*
import scala.sys.env

type MetadataFunction = Option[String] => Unit
type SimpleFunction   = () => Unit

sealed trait HandlerFunction {
  val action: String
}
case class MetadataHandlerFunction(action: String, function: MetadataFunction)
    extends HandlerFunction
case class SimpleHandlerFunction(action: String, function: SimpleFunction)
    extends HandlerFunction

trait HandlerDsl extends Plugin {
  var handlers: Map[String, HandlerFunction] = Map.empty

  private def build(): Handler = handlers
    .foldLeft(PartialFunction.empty[(String, Option[String]), Unit])(
      (acc, cur) => {
        val p: Handler = cur._2 match {
          case SimpleHandlerFunction(a, f) => {
            case (a: String, _) if a == cur._1 => f()
          }
          case MetadataHandlerFunction(a, f) => {
            case (a: String, m: Option[String]) if a == cur._1 => f(m)
          }
        }
        acc orElse p
      }
    )
    .orElse(PartialFunction.fromFunction(_ => ()))

  type HandlerContextFunction = HandlerBuilder ?=> Unit

  class HandlerBuilder {
    def add(item: HandlerFunction) = {
      item match {
        case m @ MetadataHandlerFunction(action, function) =>
          handlers = handlers + (action -> m)
        case s @ SimpleHandlerFunction(action, function) =>
          handlers = handlers + (action -> s)
      }
    }

    override def toString =
      s"${handlers.size} handler(s)${handlers.keys.mkString(": [", ",", "]")}"
  }

  def handler(init: HandlerContextFunction): HandlerBuilder = {
    given t: HandlerBuilder = HandlerBuilder()
    init
    t
  }

  override def appHandler: Handler = build()

  def handle(
      action: String
  )(metadataF: MetadataFunction): HandlerContextFunction = {
    val handlerBuilder: HandlerBuilder = summon[HandlerBuilder]
    handlerBuilder.add(MetadataHandlerFunction(action, metadataF))
  }

  def handle(action: String)(f: => Unit): HandlerContextFunction = {
    summon[HandlerBuilder].add(SimpleHandlerFunction(action, () => f))
  }
}
