package edu.mtsde.dto.json

import edu.mtsde.dto.{ImdbMovie, MovieState, NewMovie, TicketRequest}
import org.scalatest.FlatSpec

class DtoJsonFormatTest extends FlatSpec with JsonSupport {
  import spray.json._

  "DTO format" should "parse new movie object from JSON" in {
    val json =
      """
        |{
        |  "imdbId": "tt0111161",
        |  "availableSeats": 100,
        |  "screenId": "screen_123456"
        |}
      """.stripMargin
    val expected = NewMovie("tt0111161", "screen_123456", 100)

    val actual = json.parseJson.convertTo[NewMovie]

    assert(expected === actual)
  }

  it should "parse ticket request object from JSON" in {
    val json =
      """
        |{
        |  "imdbId": "tt0111161",
        |  "screenId": "screen_123456"
        |}
      """.stripMargin

    val expected = json.parseJson.convertTo[TicketRequest]

    val actual = TicketRequest("tt0111161", "screen_123456")

    assert(expected === actual)
  }

  it should "serialize movie state to JSON" in {
    val expected =
      "{" +
        "\"reservedSeats\":50," +
        "\"screenId\":\"screen_123456\"," +
        "\"imdbId\":\"tt0111161\"," +
        "\"availableSeats\":100," +
        "\"movieTitle\":\"The Shawshank Redemption\"" +
      "}"

    val movieState = MovieState("tt0111161", "screen_123456", "The Shawshank Redemption", 100, 50)

    val json = movieState.toJson.toString

    assert(expected === json)
  }

  it should "match movie title from IMDB response" in {
    assert(imdbResponse.parseJson.convertTo[ImdbMovie] === ImdbMovie("The Shawshank Redemption"))
  }

  val imdbResponse = """{
                       |    "Actors": "Tim Robbins, Morgan Freeman, Bob Gunton, William Sadler",
                       |    "Awards": "Nominated for 7 Oscars. Another 19 wins & 30 nominations.",
                       |    "BoxOffice": "N/A",
                       |    "Country": "USA",
                       |    "DVD": "27 Jan 1998",
                       |    "Director": "Frank Darabont",
                       |    "Genre": "Crime, Drama",
                       |    "Language": "English",
                       |    "Metascore": "80",
                       |    "Plot": "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                       |    "Poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1_SX300.jpg",
                       |    "Production": "Columbia Pictures",
                       |    "Rated": "R",
                       |    "Ratings": [
                       |        {
                       |            "Source": "Internet Movie Database",
                       |            "Value": "9.3/10"
                       |        },
                       |        {
                       |            "Source": "Rotten Tomatoes",
                       |            "Value": "91%"
                       |        },
                       |        {
                       |            "Source": "Metacritic",
                       |            "Value": "80/100"
                       |        }
                       |    ],
                       |    "Released": "14 Oct 1994",
                       |    "Response": "True",
                       |    "Runtime": "142 min",
                       |    "Title": "The Shawshank Redemption",
                       |    "Type": "movie",
                       |    "Website": "N/A",
                       |    "Writer": "Stephen King (short story \"Rita Hayworth and Shawshank Redemption\"), Frank Darabont (screenplay)",
                       |    "Year": "1994",
                       |    "imdbID": "tt0111161",
                       |    "imdbRating": "9.3",
                       |    "imdbVotes": "1,803,191"
                       |}""".stripMargin
}
