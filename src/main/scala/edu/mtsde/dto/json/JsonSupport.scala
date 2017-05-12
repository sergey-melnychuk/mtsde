package edu.mtsde.dto.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import edu.mtsde.dto.{ImdbMovie, MovieState, NewMovie, TicketRequest}
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val newMovieFormat = jsonFormat3(NewMovie)
  implicit val ticketRequestFormat = jsonFormat2(TicketRequest)
  implicit val movieStateFormat = jsonFormat5(MovieState)
  implicit val imdbMovieFormat = jsonFormat1(ImdbMovie)
}
