package edu.mtsde.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.{ask, pipe}
import edu.mtsde.domain.Movie

import scala.util.control.NonFatal

class MovieManagerActor extends Actor with ActorLogging {
  import edu.mtsde.actors.MovieManagerActor._
  import context.dispatcher  // required for map/recover on Future as implicit ExecutionContext

  def receive = {
    case AddMovie(movie) =>
      try {
        val ref = context.actorOf(MovieStateActor.props(movie), movie.imdbId)
        sender ! MovieAddedSuccessfully(movie, ref)
      } catch {
        case NonFatal(e) =>
          sender ! MovieNotAdded(e.getMessage)
      }

    case GetMovie(imdbId: String) =>
      val futureResponse = context.actorSelection(imdbId)
        .ask(MovieStateActor.GetMovieState)
        .mapTo[MovieStateActor.MovieState]
        .map(ms => MovieFound(ms.movie))
        .recover({ case NonFatal(e) => MovieNotFound(imdbId) })

      val capturedSender = sender()
      pipe(futureResponse).pipeTo(capturedSender)
  }
}

object MovieManagerActor {
  def props: Props = Props[MovieManagerActor]

  case class AddMovie(movie: Movie)
  case class GetMovie(imdbId: String)

  case class MovieAddedSuccessfully(movie: Movie, ref: ActorRef)
  case class MovieNotAdded(reason: String)

  case class MovieFound(movie: Movie)
  case class MovieNotFound(imdbId: String)
}
