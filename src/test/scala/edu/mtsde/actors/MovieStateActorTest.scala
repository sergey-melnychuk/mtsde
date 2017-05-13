package edu.mtsde.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import edu.mtsde.domain.{Movie, Ticket}
import edu.mtsde.persistence.MovieRepository
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class MovieStateActorTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val movieRepository = MovieRepository("memory")

  "A MovieStateActor" must {

    "respond with issued ticket" in {
      val availableTickets = 1
      val movie = Movie(imdbId = "1", title = "Test Movie", screenId = "0", availableTickets, Nil)
      val movieStateActor = system.actorOf(MovieStateActor.props(movie, movieRepository))

      movieStateActor ! MovieStateActor.RequestTicket

      expectMsg(MovieStateActor.TicketRequestSucceed(Ticket(1)))
    }

    "respond with failure if no tickets left" in {
      val availableTickets = 0
      val movie = Movie(imdbId = "1", title = "Test Movie", screenId = "0", availableTickets, Nil)
      val movieStateActor = system.actorOf(MovieStateActor.props(movie, movieRepository))

      movieStateActor ! MovieStateActor.RequestTicket

      expectMsg(MovieStateActor.TicketRequestFailed("No more tickets to issue"))
    }

  }
}
