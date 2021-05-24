package com.bilalfazlani.swiftbar4s.utils

import java.net.{HttpURLConnection, URL}
import scala.util.Try
import scala.quoted.*
import scala.sys.process.BasicIO
import java.io.{File, FileOutputStream, FileWriter}
import java.nio.charset.Charset
import scala.quoted.Expr
import scala.quoted.Quotes

object HttpClient {
  private val connectTimeout = 15000
  private val readTimeout    = 15000
  private val requestMethod  = "GET"

  // inline def getBytesInline(inline url: String) = ${fetchMacro('url)}

  // def fetchMacro(url:Expr[String])(using Quotes):Expr[Option[Array[Byte]]] =
  //   Expr(getBytes(url.valueOrError))

  def getBytes(url:String): Option[Array[Byte]] = Try {
      val connection =
        (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
      connection.setConnectTimeout(connectTimeout)
      connection.setReadTimeout(readTimeout)
      connection.setRequestMethod(requestMethod)
      for {
        stream <- Option(connection.getInputStream)
        bytes = stream.readAllBytes()
        _ = stream.close()
      } yield bytes
    }.toOption.flatten
}
