package com.bilalfazlani.swiftbar4s.dsl

import com.bilalfazlani.swiftbar4s.Plugin

trait PluginDsl
    extends Plugin
    with MenuDsl
    with HandlerDsl
    with NotificationDsl
    with Environment
