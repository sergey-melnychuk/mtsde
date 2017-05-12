package edu.mtsde.dto

final case class MovieState(imdbId: String, screenId: String, movieTitle: String, availableSeats: Int, reservedSeats: Int)
