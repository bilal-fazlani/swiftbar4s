package com.bilalfazlani.scalabar.dsl

import scala.collection.mutable.ListBuffer
import com.bilalfazlani.scalabar.models.MenuItem
import com.bilalfazlani.scalabar.Handler
import com.bilalfazlani.scalabar.models.Attribute
import com.bilalfazlani.scalabar.models.Attribute._
import com.bilalfazlani.scalabar.models.MenuItem._
import scala.sys.env

type MetadataFunction = Option[String] => Unit
type SimpleFunction = () => Unit

sealed trait HandlerFunction{
  val action:String
}
case class MetadataHandlerFunction(action:String, function: MetadataFunction) extends HandlerFunction
case class SimpleHandlerFunction(action: String, function: SimpleFunction) extends HandlerFunction

class HandlerBuilder {
  var handlers: Map[String, HandlerFunction] = Map.empty
  
  def add(item: HandlerFunction) = {
    item match {
      case m @ MetadataHandlerFunction(action, function) => 
        handlers = handlers + (action -> m)
      case s @ SimpleHandlerFunction(action, function) =>
      handlers = handlers + (action -> s)
    }
  }

  def build(): Handler = handlers.foldLeft(PartialFunction.empty[(String, Option[String]), Unit])((acc, cur) => {
      val p:Handler = cur._2 match {
        case SimpleHandlerFunction(a, f) => {
            case (a: String, _) if a == cur._1 => f()
          }
        case MetadataHandlerFunction(a, f) => {
          case (a: String, m: Option[String]) if a == cur._1 => f(m)
        }
      }
      acc orElse p
      }
    ).orElse(PartialFunction.fromFunction(_ => ()))

  override def toString = s"${handlers.size} handler(s)${handlers.keys.mkString(": [",",","]")}"
}

trait HandlerDsl {
  def handler(init: ContextFunction[HandlerBuilder]): HandlerBuilder = {
    given t:HandlerBuilder = HandlerBuilder()
    init
    t
  }

  def handle(action: String)(metadataF : MetadataFunction): ContextFunction[HandlerBuilder] = {   
    val handlerBuilder: HandlerBuilder = summon[HandlerBuilder]  
    handlerBuilder.add(MetadataHandlerFunction(action, metadataF))
  }

  def handle(action: String)(f : => Unit): ContextFunction[HandlerBuilder] = {      
    summon[HandlerBuilder].add(SimpleHandlerFunction(action, () => f))
  }
}