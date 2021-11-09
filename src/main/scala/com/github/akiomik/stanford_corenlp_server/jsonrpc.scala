package com.github.akiomik.stanford_corenlp_server

import io.finch.Output
import shapeless._
import com.twitter.finagle.http.Status

trait JsonRpc {
  val v20 = "2.0"

  // TODO: support batch request
  type Param = Double :+: String :+: Boolean :+: CNil
  type Params = Seq[Param] :+: Map[String, Param] :+: CNil
  case class Request(jsonrpc: String, method: String, params: Option[Params], id: Option[String])

  case class Error(code: Int, message: String, data: Option[String])
  def ParseError     = Error(-32700, _: String, _: Option[String])
  def InvalidRequest = Error(-32600, _: String, _: Option[String])
  def MethodNotFound = Error(-32601, _: String, _: Option[String])
  def InvalidParams  = Error(-32602, _: String, _: Option[String])
  def InternalError  = Error(-32603, _: String, _: Option[String])

  // sealed trait Response[A]
  // case object Empty extends Response[Nothing]
  // case class Success[A](jsonrpc: String, result: A, id: Option[String]) extends Response[A]
  // case class Failure(jsonrpc: String, error: Error, id: Option[String]) extends Response[Nothing]
  case class Response[+A](jsonrpc: String, result: Option[A], error: Option[Error], id: Option[String])
  def Payload[A](jsonrpc: String, a: A, id: Option[String]) = Response(jsonrpc, Some(a), None, id)
  def Failure(jsonrpc: String, error: Error, id: Option[String]) = Response(jsonrpc, None, Some(error), id)

  // output wrapper
  def BadRequest          = Output.Payload(_: Response[Nothing], Status.BadRequest)
  def InternalServerError = Output.Payload(_: Response[Nothing], Status.InternalServerError)
}

