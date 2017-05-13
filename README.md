Movie Ticket System Design Exercise
===================================

### Description
Simple but consistent and scalable system that allows following:
* Create a movie entity with available number of tickets
* Reserve a ticket for a given movie
* Get a current status of a given movie

Interface is simple RESTful-like JSON-over-HTTP API build on Akka HTTP.

### Goals
* Consistency - all requests to given movie are linearized, single actor stores state for a movie
* Durability - all updates are stored to the persistent storage in document-oriented manner
* Persistence - MongoDB was chosen as a scalable document-oriented database
* Isolation - the 'Transaction' entity with own lifecycle is being created for each reservation request
* Scalability - sharding of independent movies' states across nodes gives horizontal scalability

### JSONs
##### POST new movie
```json
{
  "imdbId": "tt0111161",
  "availableSeats": 100,
  "screenId": "screen_123456"
}
```

##### POST new ticket request
```json
{
  "imdbId": "tt0111161",
  "screenId": "screen_123456"
}
```

##### GET current state
```json
{
  "imdbId": "tt0111161",
  "screenId": "screen_123456",
  "movieTitle": "The Shawshank Redemption",
  "availableSeats": 100,
  "reservedSeats": 50
}
```

### IMDB lookup
```bash
$ curl "http://www.omdbapi.com/?i=tt0111161" | python -m json.tool
```
```json
{
    "Actors": "Tim Robbins, Morgan Freeman, Bob Gunton, William Sadler",
    "Awards": "Nominated for 7 Oscars. Another 19 wins & 30 nominations.",
    "BoxOffice": "N/A",
    "Country": "USA",
    "DVD": "27 Jan 1998",
    "Director": "Frank Darabont",
    "Genre": "Crime, Drama",
    "Language": "English",
    "Metascore": "80",
    "Plot": "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
    "Poster": "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1_SX300.jpg",
    "Production": "Columbia Pictures",
    "Rated": "R",
    "Ratings": [
        {
            "Source": "Internet Movie Database",
            "Value": "9.3/10"
        },
        {
            "Source": "Rotten Tomatoes",
            "Value": "91%"
        },
        {
            "Source": "Metacritic",
            "Value": "80/100"
        }
    ],
    "Released": "14 Oct 1994",
    "Response": "True",
    "Runtime": "142 min",
    "Title": "The Shawshank Redemption",
    "Type": "movie",
    "Website": "N/A",
    "Writer": "Stephen King (short story \"Rita Hayworth and Shawshank Redemption\"), Frank Darabont (screenplay)",
    "Year": "1994",
    "imdbID": "tt0111161",
    "imdbRating": "9.3",
    "imdbVotes": "1,803,191"
}
```

### Timeline
* 12/5, 21:30 Start: repository setup from template, brief description
* 12/5, +1h 30m DTOs and edu.mtsde.domain model ready, akka and akka-http dependencies
* 12/5, +1h Package structure, unit-tests for JSON formats, components skeletons
* 13/5, 14:00 Push changes, update readme, start implementing actors
* 13/5, +1h Completed MovieStateActor covered with unit-tests, completed MovieManagerActor
