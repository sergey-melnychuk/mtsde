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

### Example

```bash
$ sbt run
...
```

```bash
$ curl -H 'Content-Type: application/json' -d'{"imdbId":"1","screenId":"2","availableSeats":100}' localhost:8080/movie
OK
```

```bash
$ curl localhost:8080/movie/1/2
{"reservedSeats":0,"screenId":"2","imdbId":"1","availableSeats":100,"movieTitle":"<Missing title>"}
```

```bash
$ curl -H 'Content-Type: application/json' -d'{"imdbId":"1","screenId":"2"}' localhost:8080/ticket
1
```

```bash
$ curl localhost:8080/movie/1/2
{"reservedSeats":1,"screenId":"2","imdbId":"1","availableSeats":100,"movieTitle":"<Missing title>"}
```

### Timeline
* 12/5, 21:30 Start: repository setup from template, brief description
* 12/5, +1h 30m DTOs and edu.mtsde.domain model ready, akka and akka-http dependencies
* 12/5, +1h Package structure, unit-tests for JSON formats, components skeletons
* 13/5, 14:00 Push changes, update readme, start implementing actors
* 13/5, +1h Completed MovieStateActor covered with unit-tests, completed MovieManagerActor
* 13/5, 18:30 Started akka-http based movie service, adding tests to MovieManagerActor
* 13/5, +1h Completed server skeleton, MovieManagerActor unit-tests and simple InMemoryMovieRepository
* 13/5, +1h Completed server functionality, added examples, updated timeline
* 13/5, 20:00 Completed the exercise, total time spent: 5h 30m, shortcuts described below

### Shortcuts
* For the sake of exercise, there is no point to bring MongoDB, though it's pretty straightforward
* Resolve title by id using external resource is pretty straightforward as well (simple HTTP call), skipping
* For this specific exercise there is no point for explicit test coverage of HTTP API, skipping
