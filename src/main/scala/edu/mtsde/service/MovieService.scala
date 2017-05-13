package edu.mtsde.service

import java.util.concurrent.CountDownLatch

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import edu.mtsde.dto.json.JsonSupport
import edu.mtsde.dto.{NewMovie, TicketRequest}

class MovieService(movieManagerActor: ActorRef)(implicit system: ActorSystem) extends JsonSupport {
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val latch = new CountDownLatch(1)

  val route: Route =
    get {
      pathPrefix("movie" / Segment / Segment) { (imdbId, screenId) =>
        complete(s"imdbId: ${imdbId}, screenId: ${screenId}\n")
      }
    } ~
    post {
      path("movie") {
        entity(as[NewMovie]) { movie =>
          complete("movie: " + movie + "\n")
        }
      } ~
      path("ticket") {
        entity(as[TicketRequest]) { ticketRequest =>
          complete("ticket: " + ticketRequest + "\n")
        }
      }
    } ~
    delete {
      path("shutdown") {
        latch.countDown()
        complete("shutdown\n")
      }
    }

  def start(port: Int): Unit = {
    val bindingFuture = Http().bindAndHandle(route, "localhost", port)

    latch.await() // block until server is shut-down

    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
