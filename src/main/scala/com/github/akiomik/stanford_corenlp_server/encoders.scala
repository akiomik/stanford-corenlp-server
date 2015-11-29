package com.github.akiomik.stanford_corenlp_server

// stanford corenlp
import edu.stanford.nlp.pipeline.{JSONOutputter, Annotation}
import java.io.ByteArrayOutputStream

// import io.circe._
import io.circe.{Json, Encoder}
import io.circe.jawn._

trait Encoders {

  // instance for encoding annotation 
  implicit val encodeAnnotation: Encoder[Annotation] = Encoder.instance { a =>
    val output = new ByteArrayOutputStream()
    JSONOutputter.jsonPrint(a, output)
    val json = new String(output.toByteArray, "UTF-8")
    parse(json).getOrElse(Json.empty)
  }

}
