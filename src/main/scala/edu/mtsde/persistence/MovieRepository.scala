package edu.mtsde.persistence

import edu.mtsde.domain.{Movie, Ticket}

trait MovieRepository {
  def findByImdbId(imdbId: String): Option[Movie]
  def persistTicket(movie: Movie, ticket: Ticket)
}
