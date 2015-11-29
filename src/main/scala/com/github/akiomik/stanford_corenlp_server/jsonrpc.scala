package com.github.akiomik.stanford_corenlp_server

import io.finch.Output
import shapeless._
import com.twitter.finagle.http.Status

trait JsonRpc {
  val v20 = "2.0"

  case class Error(code: Int, message: String, data: Option[String])
  def ParseError     = Error(-32700, _: String, _: Option[String])
  def InvalidRequest = Error(-32600, _: String, _: Option[String])
  def MethodNotFound = Error(-32601, _: String, _: Option[String])
  def InvalidParams  = Error(-32602, _: String, _: Option[String])
  def InternalError  = Error(-32603, _: String, _: Option[String])

  // TODO: support number and boolean values
  type Params = Seq[String] :+: Map[String, String] :+: CNil
  case class Req(jsonrpc: String, method: String, params: Option[Params], id: Option[String])
  case class Res[A](jsonrpc: String, result: Option[A], error: Option[Error], id: Option[String])

  // output wrapper
  def BadRequest[A]          = Output.Payload(_: Res[A], Status.BadRequest)
  def InternalServerError[A] = Output.Payload(_: Res[A], Status.InternalServerError)
}

