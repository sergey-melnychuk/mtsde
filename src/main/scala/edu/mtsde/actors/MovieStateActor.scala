package edu.mtsde.actors

import akka.actor.{Actor, ActorLogging, Props}
import edu.mtsde.domain.{Movie, Ticket}

class MovieStateActor(initialMovie: Movie) extends Actor with ActorLogging {
  import edu.mtsde.actors.MovieStateActor._

  var movie = initialMovie
  var ticketsIssued = 0

  def nextTicketNumber(): Int = {
    ticketsIssued += 1
    ticketsIssued
  }

  def canIssueTicket(): Boolean = {
    ticketsIssued < movie.availableSeats
  }

  def appendTicket(ticket: Ticket): Movie = {
    movie.copy(tickets = ticket :: movie.tickets)
  }

  def receive = {
    case RequestTicket if canIssueTicket() =>
      val ticket = Ticket(nextTicketNumber())
      movie = appendTicket(ticket)
      sender ! TicketRequestSucceed(ticket)

    case RequestTicket =>
      sender ! TicketRequestFailed("No more tickets to issue")

    case GetMovieState =>
      sender ! MovieState(movie)
  }
}

object MovieStateActor {
  def props(initialMovie: Movie): Props = Props(classOf[MovieStateActor], initialMovie)

  case object RequestTicket
  case class TicketRequestFailed(reason: String)
  case class TicketRequestSucceed(ticket: Ticket)

  case object GetMovieState
  case class MovieState(movie: Movie)
}
