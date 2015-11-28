package com.github.akiomik.stanford_corenlp_server

// stanford corenlp
import edu.stanford.nlp.pipeline.{JSONOutputter, Annotation}
import java.io.ByteArrayOutputStream

// import io.circe._
import io.circe.{Json, Encoder => CEncoder}
import io.circe.jawn._

trait Encoder {

  // instance for encoding annotation 
  implicit val encodeAnnotation: CEncoder[Annotation] = CEncoder.instance { a =>
    val output = new ByteArrayOutputStream()
    JSONOutputter.jsonPrint(a, output)
    val json = new String(output.toByteArray, "UTF-8")
    parse(json).getOrElse(Json.empty)
  }

}
