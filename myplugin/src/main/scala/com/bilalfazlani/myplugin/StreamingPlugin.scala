package com.bilalfazlani.myplugin

import zio.*
import com.bilalfazlani.swiftbar4s.dsl.PluginDsl
import org.reactivestreams.Publisher
import zio.stream.ZStream
import zio.interop.reactivestreams.*
import com.bilalfazlani.swiftbar4s.models.MenuItem
import scala.concurrent.duration.Duration
import scala.concurrent.Await

object StreamingPlugin extends PluginDsl {

  val effect: ZIO[Any, Nothing, Publisher[MenuItem]] =
    ZStream
      .tick(1.second)
      .zipWithIndex
      .map(item => topLevel.text(s"Item ${item._2}"))
      .take(10)
      .toPublisher

  val menuStream: Publisher[MenuItem] = Unsafe.unsafely {
    zio.Runtime.default.unsafe.run(effect).getOrThrow()
  }

  stream(menuStream)
}
