import akka.actor.ActorSystem
import edu.mtsde.actors.MovieManagerActor
import edu.mtsde.persistence.MovieRepository
import edu.mtsde.service.MovieService

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("movie")

    val repository = MovieRepository("memory")

    val manager = system.actorOf(MovieManagerActor.props(repository), "manager")

    val service = new MovieService(manager)

    service.start(8080)
  }
}
