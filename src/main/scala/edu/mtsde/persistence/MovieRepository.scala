package edu.mtsde.persistence

import edu.mtsde.domain.{Movie, Ticket}
import edu.mtsde.persistence.memory.InMemoryMovieRepository

trait MovieRepository {
  def findByImdbId(id: String): Option[Movie]
  def persistMovie(movie: Movie): Movie
  def persistTicket(movie: Movie, ticket: Ticket): Movie
}

object MovieRepository {
  def apply(): MovieRepository = new InMemoryMovieRepository

  def apply(mode: String): MovieRepository = mode match {
    case "memory" => new InMemoryMovieRepository
//    case "mongo" =>
  }
}
