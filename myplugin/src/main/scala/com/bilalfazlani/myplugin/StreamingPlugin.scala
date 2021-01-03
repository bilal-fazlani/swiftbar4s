package com.bilalfazlani.myplugin

import com.bilalfazlani.swiftbar4s.Plugin
import com.bilalfazlani.swiftbar4s.dsl._
import com.bilalfazlani.EntityClient
import com.bilalfazlani._
import akka.actor.typed._
import akka.actor.typed.scaladsl._
import akka.stream.scaladsl._
import com.bilalfazlani.responses.Event


object StreamingPlugin extends Plugin with MenuDsl {
    given actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "actorSystem")
    import actorSystem.executionContext
    val client = EntityClient("localhost", 9090)

    def menuTitle(event:Event):String = event match {
        case Event.Init(data) if data.isEmpty => "empty"        
        case Event.Added(_, data) if data.isEmpty => "empty"
        case Event.Deleted(_, data) if data.isEmpty => "empty"
        case Event.Reset(_) => "empty"

        case Event.Added(_, _) | Event.Init(_) | Event.Deleted(_, _) => "entities"
    }

    override val appMenu = client.subscribe.block.map(event => menu(menuTitle(event)){
        def renderMenuItems(entities:Set[Entity]):Unit = 
            entities.foreach(e => text(s"${e.id} - ${e.name}"))

        event match {
            case Event.Init(data) if data.nonEmpty => renderMenuItems(data)
            case Event.Added(_, data) if data.nonEmpty => renderMenuItems(data)
            case Event.Deleted(_, data) if data.nonEmpty => renderMenuItems(data)
            case _ => 
        }
    }).runWith(Sink.asPublisher(false))
}