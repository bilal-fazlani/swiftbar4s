package com.bilalfazlani.scalabar.dsl

import com.bilalfazlani.scalabar.models.Tag
import com.bilalfazlani.scalabar.models.Tag._

class TagBuilder {
    private var tags:Set[Tag] = Set.empty
    def add(tag:Tag) = tags = tags + tag
    def build = tags
}

trait TagDsl {
    def tags(init: ContextFunction[TagBuilder]): TagBuilder = {
      given t:TagBuilder = TagBuilder()
      init
      t
    }

    def author(value:String): ContextFunction[TagBuilder] = {
        summon[TagBuilder].add(Author(value))
    }

}