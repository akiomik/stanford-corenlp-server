package com.github.akiomik.stanford_corenlp_server

// typesafe config
import com.typesafe.config.ConfigFactory

// stanford corenlp
import edu.stanford.nlp.pipeline.{StanfordCoreNLP, JSONOutputter, Annotation}

// json parser
import io.circe._
import io.circe.generic.auto._
import io.circe.jawn._
import java.io.ByteArrayOutputStream

// finch
import io.finch._
import io.finch.request._
import io.finch.circe._
import com.twitter.finagle.Http
import com.twitter.util.Await
import com.twitter.util.FuturePool

// twitter server
import com.twitter.server.TwitterServer

object Main extends TwitterServer {

  // flags
  val port = flag("port", ":8081", "http server port")

  val props    = ConfigFactory.load.toProps
  val pipeline = new StanfordCoreNLP(props)

  // instance for encoding annotation 
  implicit val encodeAnnotation: Encoder[Annotation] = Encoder.instance { a =>
    val output = new ByteArrayOutputStream()
    JSONOutputter.jsonPrint(a, output)
    val json = new String(output.toByteArray, "UTF-8")
    parse(json).getOrElse(Json.empty)
  }

  val rpc = post("rpc" ? body.as[Req]) { req: Req =>
    FuturePool.unboundedPool { // wapper for expensive computations
      val params = req.params.getOrElse(Seq())
      params match {
        case Seq(a) => {
          log.debug(s"text: $a")
          val anno = pipeline.process(a)
          Ok(Res("2.0", Some(anno), None, req.id))
        }
        case _ => Ok(Res("2.0", None, Some(InvalidParams("", None)), req.id))
      }
    }
  }
  val api = rpc

  def main() {
    val server = Http.server.serve(port(), api.toService)
    onExit { server.close() }

    Await.ready(adminHttpServer)
  }

}
