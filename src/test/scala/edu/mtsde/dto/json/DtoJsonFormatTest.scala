package edu.mtsde.dto.json

import edu.mtsde.dto.{MovieState, NewMovie, TicketRequest}
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

}
