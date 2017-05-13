package edu.mtsde.service

import scala.language.postfixOps
import java.util.concurrent.CountDownLatch

import scala.concurrent.duration._
import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import edu.mtsde.actors.{MovieManagerActor, MovieStateActor}
import edu.mtsde.actors.MovieManagerActor.{AddMovieResult, GetMovieResult}
import edu.mtsde.domain.Movie
import edu.mtsde.dto.json.JsonSupport
import edu.mtsde.dto.{MovieState, NewMovie, TicketRequest}

class MovieService(movieManagerActor: ActorRef)(implicit system: ActorSystem) extends JsonSupport {
  import spray.json._

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val timeout: Timeout = 3 seconds

  val latch = new CountDownLatch(1)

  val route: Route =
    get {
      pathPrefix("movie" / Segment / Segment) { (imdbId, screenId) =>
        val result = movieManagerActor.ask(MovieManagerActor.GetMovie(imdbId, screenId)).mapTo[GetMovieResult]
        complete {
          result.map({
            case MovieManagerActor.MovieFound(movie) =>
              val dto = MovieState(movie.imdbId, movie.screenId, movie.title, movie.availableSeats, movie.tickets.size)
              dto.toJson.toString
            case MovieManagerActor.MovieNotFound(id) =>
              "No such movie"
          })
        }
      }
    } ~
    post {
      path("movie") {
        entity(as[NewMovie]) { movieDto =>
          val movie = Movie(movieDto.imdbId, "<Missing title>", movieDto.screenId, movieDto.availableSeats, Nil)
          val result = movieManagerActor.ask(MovieManagerActor.AddMovie(movie)).mapTo[AddMovieResult]
          complete {
            result.map({
              case MovieManagerActor.MovieAddedSuccessfully(_, _) => "OK"
              case MovieManagerActor.MovieNotAdded(reason) => reason
            })
          }
        }
      } ~
      path("ticket") {
        entity(as[TicketRequest]) { ticketRequest =>
          val result = system.actorSelection(s"akka://movie/user/manager/${ticketRequest.imdbId}.${ticketRequest.screenId}")
            .ask(MovieStateActor.RequestTicket)
            .mapTo[MovieStateActor.RequestTicketResult]
            .map({
              case MovieStateActor.TicketRequestSucceed(ticket) =>
                ticket.ticketNr.toString
              case MovieStateActor.TicketRequestFailed(reason) =>
                reason
            })
          complete(result)
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
