package com.bilalfazlani.swiftbar4s

import com.bilalfazlani.swiftbar4s.models.*
import com.bilalfazlani.swiftbar4s.models.MenuItem.*
import com.bilalfazlani.swiftbar4s.parser.{StreamingMenuRenderer}
import com.bilalfazlani.swiftbar4s.dsl.*
import org.reactivestreams.*

class MenuSubscriber(menuRenderer: StreamingMenuRenderer)
    extends Subscriber[MenuBuilder] {
  var sub: Option[Subscription] = None

  override def onSubscribe(subscription: Subscription): Unit = {
    sub = Some(subscription)
    sub.get.request(1)
  }

  override def onNext(item: MenuBuilder): Unit = {
    sub.foreach { s =>
      menuRenderer.renderMenu(item.build, true)
      s.request(1)
    }
  }

  override def onError(t: Throwable): Unit =
    t.printStackTrace(System.err)
    sys.exit(1)

  override def onComplete(): Unit = sys.exit(0)
}
