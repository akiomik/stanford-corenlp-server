package com.github.akiomik.stanford_corenlp_server

import edu.stanford.nlp.pipeline.Annotation

trait JsonRpc {
  case class Error(code: Int, message: String, data: Option[String])
  def InvalidParams = Error(-32602, _: String, _: Option[String])

  // TODO: support object params
  case class Req(jsonrpc: String, method: String, params: Option[Seq[String]], id: Option[String])
  // TODO: generalize
  case class Res(jsonrpc: String, result: Option[Annotation], error: Option[Error], id: Option[String])
}

