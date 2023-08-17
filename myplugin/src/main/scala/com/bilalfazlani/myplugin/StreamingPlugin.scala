package com.bilalfazlani.myplugin

import zio.*
import com.bilalfazlani.swiftbar4s.dsl.PluginDsl
import zio.stream.ZStream
import com.bilalfazlani.swiftbar4s.models.MenuItem
import com.bilalfazlani.swiftbar4s.dsl.MenuBuilder

object StreamingPlugin extends PluginDsl, ZStreamSupport {
  stream {
    ZStream
      .tick(500.millis)
      .zipWithIndex
      .map(item =>
        menu(s"Item ${item._2}") {
          subMenu("Hello") {
            text("World")
          }
        }
      )
      .take(10)
  }
}

trait ZStreamSupport {
  import zio.interop.reactivestreams.streamToPublisher
  import org.reactivestreams.Publisher

  given a[A]: Conversion[ZStream[Any, Throwable, A], Publisher[A]] = stream =>
    Unsafe.unsafely {
      zio.Runtime.default.unsafe.run(stream.toPublisher).getOrThrow()
    }

  given b
      : Conversion[ZStream[Any, Throwable, MenuBuilder], Publisher[MenuItem]] =
    stream => stream.map(_.build)
}
