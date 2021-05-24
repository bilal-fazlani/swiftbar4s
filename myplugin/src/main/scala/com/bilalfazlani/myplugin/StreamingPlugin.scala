//package com.bilalfazlani.myplugin
//
//import akka.actor.typed.scaladsl.Behaviors
//import akka.actor.typed.ActorSystem
//import com.bilalfazlani.EntityClient
//import com.bilalfazlani.responses.Event
//import com.bilalfazlani.swiftbar4s.dsl.*
//import scala.concurrent.ExecutionContext.Implicits.global
//
//object StreamingPlugin extends PluginDsl {
//    given actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "actorSystem")
//    val client = EntityClient("localhost", 9090)
//
//    def menuTitle(event:Event):String = event match {
//        case Event.Init(data) if data.isEmpty => "empty"
//        case Event.Added(_, data) if data.isEmpty => "empty"
//        case Event.Deleted(_, data) if data.isEmpty => "empty"
//        case Event.Reset(_) => "empty"
//
//        case Event.Added(_, _) | Event.Init(_) | Event.Deleted(_, _) => "entities"
//    }
//
//    private def notification(event:Event) = event match {
//        case Event.Added(entity,_ ) => notify("entity added", Some(entity.name), Some(entity.id.toString))
//        case Event.Deleted(entity, _) => notify("entity deleted", Some(entity.name), Some(entity.id.toString))
//        case Event.Reset(_) => notify("reset")
//        case _ =>
//    }
//
//    client.subscribe.block.map(event => menu(menuTitle(event)){
//        notification(event)
//
//        def renderMenuItems(entities:Set[Entity]):Unit =
//            entities.foreach(e => text(s"${e.id} - ${e.name}"))
//
//        event match {
//            case Event.Init(data) if data.nonEmpty => renderMenuItems(data)
//            case Event.Added(_, data) if data.nonEmpty => renderMenuItems(data)
//            case Event.Deleted(_, data) if data.nonEmpty => renderMenuItems(data)
//            case _ =>
//        }
//    }).runWith(Sink.asPublisher(false))
//}
