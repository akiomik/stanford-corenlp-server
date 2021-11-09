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
    implicit def caseMap = at[Map[String, Param]] { _.get("text") }
    implicit def caseSeq = at[Seq[Param]] { _.headOption }
  }

  // val rpc: Endpoint[Response[Annotation]] = post("rpc" ? body.as[Request]) { req: Request =>
  val rpc = post("rpc" ? body.as[Request]) { req: Request =>
    FuturePool.unboundedPool { // wapper for expensive computations
      req match {
        // case Request(v20, _, _, None) => {
        //   NoContent(Empty)
        // }
        // case Request(v20, _, Some(ps), id) => ps.fold(extract) match {
        //   case Some(p) => p.select[String] match {
        //     case Some(s) => {
        //       log.debug(s"text: $s")
        //       Ok(Success(v20, pipeline.process(s), id))
        //     }
        //     case None => {
        //       BadRequest(Failure(v20, InvalidParams("value of `params` must be string.", None), id))
        //     }
        //   }
        //   case None => {
        //     BadRequest(Failure(v20, InvalidParams("`params` is invalid.", None), id))
        //   }
        // }
        case Request(v20, _, None, id) => {
          BadRequest(Failure(v20, InvalidParams("`params` is missing.", None), id))
        }
        case _ => {
          BadRequest(Failure(v20, InvalidRequest("request is invalid.", None), None))
        }
      }
    }
  // } handle {
  //   case e: NotParsed => BadRequest(Failure(v20, ParseError(e.toString, None), None))
  //   case e => {
  //     log.error(e.toString)
  //     InternalServerError(Failure(v20, InternalError(e.toString, None), None))
  //   }
  }
  val api = rpc

  def main() {
    val server = Http.server.serve(port(), api.toService)
    onExit { server.close() }

    Await.ready(adminHttpServer)
  }

}
