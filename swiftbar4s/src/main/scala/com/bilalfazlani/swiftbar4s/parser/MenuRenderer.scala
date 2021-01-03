package com.bilalfazlani.swiftbar4s.parser

import com.bilalfazlani.swiftbar4s.dsl._

class MenuRenderer(parser: Parser) {
    def renderMenu(menuBuilder: MenuBuilder, streaming:Boolean) = 
        if(streaming) println("~~~")
        parser.parse(menuBuilder.build)
          .lines
          .foreach(println)
        menuBuilder.build
}