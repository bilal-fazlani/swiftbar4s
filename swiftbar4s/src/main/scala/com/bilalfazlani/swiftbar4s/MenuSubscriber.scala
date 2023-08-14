package com.bilalfazlani.swiftbar4s

import com.bilalfazlani.swiftbar4s.models.*
import com.bilalfazlani.swiftbar4s.models.MenuItem.*
import com.bilalfazlani.swiftbar4s.parser.{StreamingMenuRenderer}
import com.bilalfazlani.swiftbar4s.dsl.*
import org.reactivestreams.*
import scala.concurrent.Promise
import scala.util.Failure
import scala.util.Success

class MenuSubscriber(
    menuRenderer: StreamingMenuRenderer,
    completionPromise: Promise[Unit]
) extends Subscriber[MenuItem] {
  var sub: Option[Subscription] = None

  override def onSubscribe(subscription: Subscription): Unit = {
    sub = Some(subscription)
    sub.get.request(1)
  }

  override def onNext(item: MenuItem): Unit = {
    sub.foreach { s =>
      menuRenderer.renderMenu(item, true)
      s.request(1)
    }
  }

  override def onError(t: Throwable): Unit =
    t.printStackTrace(System.err)
    completionPromise.complete(Failure(t))
    sys.exit(1)

  override def onComplete(): Unit =
    completionPromise.complete(Success(()))
    sys.exit(0)
}
