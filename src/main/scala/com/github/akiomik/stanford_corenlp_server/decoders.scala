package com.github.akiomik.stanford_corenlp_server

import io.circe.{Decoder, DecodingFailure}
import cats.data.Xor
import shapeless._

trait Decoders {

  // instance for decoding param
  implicit val decodeParam: Decoder[Param] = Decoder.instance { c =>
    c.focus match {
      case n if n.isNumber  => Xor.right(Coproduct[Param](n.asNumber.get.toDouble))
      case b if b.isBoolean => Xor.right(Coproduct[Param](b.asBoolean.get))
      case s if s.isString  => Xor.right(Coproduct[Param](s.asString.get))
      case _                => Xor.left(DecodingFailure(s"${c.focus.name} is unsupport value type", c.history))
    }
  }

  // instance for decoding params
  implicit val decodeParams: Decoder[Params] = Decoder.instance { c => 
    c.field("params").focus match {
      case Some(ps) if ps.isObject => ps.as[Map[String, Param]].map(Coproduct[Params](_))
      case Some(ps) if ps.isArray  => ps.as[Seq[Param]].map(Coproduct[Params](_))
      case Some(ps)                => Xor.left(DecodingFailure("`params` must be object or array", c.history))
      case None                    => Xor.left(DecodingFailure("`params` is not found", c.history))
    }
  }

}

