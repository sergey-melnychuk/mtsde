package edu.mtsde.actors

import scala.language.postfixOps
import scala.concurrent.duration._
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import edu.mtsde.domain.Movie
import edu.mtsde.persistence.MovieRepository

import scala.util.control.NonFatal

class MovieManagerActor(movieRepository: MovieRepository) extends Actor with ActorLogging {
  import edu.mtsde.actors.MovieManagerActor._
  import context.dispatcher  // required for map/recover on Future as implicit ExecutionContext

  implicit val timeout: Timeout = 1 second

  def receive = {
    case AddMovie(movie) =>
      try {
        movieRepository.persistMovie(movie)
        val ref = context.actorOf(MovieStateActor.props(movie, movieRepository), movie.id)
        sender ! MovieAddedSuccessfully(movie, ref)
      } catch {
        case NonFatal(e) =>
          sender ! MovieNotAdded(e.getMessage)
      }

    case GetMovie(imdbId: String, screenId: String) =>
      val id = imdbId + "." + screenId
      val futureResponse = context.actorSelection(id)
        .ask(MovieStateActor.GetMovieState)
        .mapTo[MovieStateActor.MovieState]
        .map(ms => MovieFound(ms.movie))
        .recover({ case NonFatal(e) => MovieNotFound(id) })

      val capturedSender = sender()
      pipe(futureResponse).pipeTo(capturedSender)
  }
}

object MovieManagerActor {
  def props(movieRepository: MovieRepository): Props = Props(classOf[MovieManagerActor], movieRepository)

  case class AddMovie(movie: Movie)
  case class GetMovie(imdbId: String, screenId: String)

  case class MovieAddedSuccessfully(movie: Movie, ref: ActorRef)
  case class MovieNotAdded(reason: String)

  case class MovieFound(movie: Movie)
  case class MovieNotFound(id: String)
}
