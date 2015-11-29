package com.github.akiomik.stanford_corenlp_server

// typesafe config
import com.typesafe.config.ConfigFactory

// stanford corenlp
import edu.stanford.nlp.pipeline.{StanfordCoreNLP, Annotation}

// finch
import io.finch._
import io.finch.request._
import io.finch.circe._
import io.circe.generic.auto._
import com.twitter.finagle.Http
import com.twitter.util.{Await, FuturePool}
import shapeless._

// twitter server
import com.twitter.server.TwitterServer

object Main extends TwitterServer {

  // flags
  val port = flag("port", ":8081", "http server port")

  val props    = ConfigFactory.load.toProps
  val pipeline = new StanfordCoreNLP(props)

  object extract extends Poly1 {
    implicit def caseMap = at[Map[String, String]] { _.get("text") }
    implicit def caseSeq = at[Seq[String]] { _.headOption }
  }

  val rpc = post("rpc" ? body.as[Req]) { req: Req =>
    FuturePool.unboundedPool { // wapper for expensive computations
      req match {
        case Req(v20, _, _, None) => {
          // notification
          NoContent(Res[Annotation](v20, None, None, None))
        }
        case Req(v20, _, Some(params), id) => {
          params.fold(extract) match {
            case Some(a) => {
              log.debug(s"text: $a")
              val anno = pipeline.process(a)
              Ok(Res(v20, Some(anno), None, id))
            }
            case None => {
              val error = InvalidParams("`params` is invalid.", None)
              BadRequest(Res[Annotation](v20, None, Some(error), id))
            }
          }
        }
        case Req(v20, _, None, id) => {
          val error = InvalidParams("`params` is missing.", None)
          BadRequest(Res[Annotation](v20, None, Some(error), id))
        }
        case _ => {
          val error = InvalidRequest("request is invalid.", None)
          BadRequest(Res[Annotation](v20, None, Some(error), None))
        }
      }
    }
  } handle {
    case e: NotParsed => {
      val error = ParseError(e.toString, None)
      BadRequest(Res[Annotation](v20, None, Some(error), None))
    }
    case e => {
      log.error(e.toString)
      val error = InternalError(e.toString, None)
      InternalServerError(Res[Annotation](v20, None, Some(error), None))
    }
  }
  val api = rpc

  def main() {
    val server = Http.server.serve(port(), api.toService)
    onExit { server.close() }

    Await.ready(adminHttpServer)
  }

}
