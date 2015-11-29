package com.github.akiomik.stanford_corenlp_server

import io.circe.{Decoder, DecodingFailure}
import cats.data.Xor
import shapeless._

trait Decoders {

  // instance for decoding params
  implicit val decodeParams: Decoder[Params] = Decoder.instance { c => 
    c.field("params").focus match {
      case Some(params) if params.isObject => {
        params.as[Map[String, String]].map(Coproduct[Params](_))
      }
      case Some(params) if params.isArray => {
        params.as[Seq[String]].map(Coproduct[Params](_))
      }
      case Some(params) => {
        Xor.left(DecodingFailure("`params` must be object or array.", c.history))
      }
      case None => {
        Xor.left(DecodingFailure("`params` is not found.", c.history))
      }
    }
  }

}

