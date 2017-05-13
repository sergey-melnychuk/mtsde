package edu.mtsde.domain

case class Movie(imdbId: String, title: String, screenId: String, availableSeats: Int, tickets: List[Ticket]) {
  val id = imdbId + "." + screenId
}
