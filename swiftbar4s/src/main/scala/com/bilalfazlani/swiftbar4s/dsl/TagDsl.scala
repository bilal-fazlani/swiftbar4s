package com.bilalfazlani.swiftbar4s.dsl

import com.bilalfazlani.swiftbar4s.models.Tag
import com.bilalfazlani.swiftbar4s.models.Tag.*

class TagBuilder {
  private var tags: Set[Tag] = Set.empty
  def add(tag: Tag)          = tags = tags + tag
  def build                  = tags
}

trait TagDsl {
  def tags(init: ContextFunction[TagBuilder]): TagBuilder = {
    given t: TagBuilder = TagBuilder()
    init
    t
  }

  def author(value: String): ContextFunction[TagBuilder] = {
    summon[TagBuilder].add(Author(value))
  }

}
