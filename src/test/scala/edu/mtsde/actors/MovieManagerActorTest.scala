package edu.mtsde.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import edu.mtsde.domain.Movie
import edu.mtsde.persistence.MovieRepository
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class MovieManagerActorTest extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  val movieRepository = MovieRepository("memory")

  "A MovieManagerActor" must {

    "respond with movie state for found movie" in {
      val movieManagerActor = system.actorOf(MovieManagerActor.props(movieRepository), "manager")

      val movie = Movie("imdbId", "title", "screen", 1, Nil)
      movieManagerActor ! MovieManagerActor.AddMovie(movie)
      expectMsgType[MovieManagerActor.MovieAddedSuccessfully]

      movieManagerActor ! MovieManagerActor.GetMovie("imdbId", "screen")
      expectMsg(MovieManagerActor.MovieFound(movie))
    }

    "respond with MovieNotFound for missing movie" in {
      val movieManagerActor = system.actorOf(MovieManagerActor.props(movieRepository), "mma")

      movieManagerActor ! MovieManagerActor.GetMovie("not found", "screen")
      expectMsg(MovieManagerActor.MovieNotFound("not found.screen"))
    }

  }
}
