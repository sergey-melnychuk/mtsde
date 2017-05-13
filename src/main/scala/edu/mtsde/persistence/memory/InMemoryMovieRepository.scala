package edu.mtsde.persistence.memory

import edu.mtsde.domain.{Movie, Ticket}
import edu.mtsde.persistence.MovieRepository

private[persistence] class InMemoryMovieRepository extends MovieRepository {
  private val data = scala.collection.mutable.HashMap.empty[String, Movie]

  override def findByImdbId(id: String): Option[Movie] = {
    data.get(id)
  }

  override def persistMovie(movie: Movie): Movie = {
    data.put(movie.id, movie)
    movie
  }

  override def persistTicket(movie: Movie, ticket: Ticket): Movie = {
    data.get(movie.id)
      .map(_.copy(tickets = ticket :: movie.tickets))
      .foreach(m => data.put(m.imdbId, m))
    data(movie.imdbId) // throw exception when persisting ticket for non-existent movie
  }
}
