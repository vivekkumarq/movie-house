# Movie House

A movie ticket booking backend split into five Spring Boot services. Users browse
what is playing in their city, pick a theatre and a show, and book seats; the
catalogue, the theatres and shows, the users, the addresses and the poster images
each live in their own service.

## Services

| Service | Port | Responsibility |
|---|---|---|
| `user-service` | 8088 | Registration, login, user profiles |
| `movie_service` | 8090 | Movie catalogue and ratings |
| `ticket-service` | 8085 | Theatres, seats, shows and bookings |
| `location-service` | 8089 | Cities and street addresses |
| `image-service` | 8800 | Poster and cover image upload and delivery |

Services find each other through Apache Zookeeper. Each one registers under its
`spring.application.name` and resolves the others through Spring Cloud's
`DiscoveryClient`, then calls them over HTTP with a `RestTemplate`. There is no
API gateway and no shared library; the calling service keeps its own copy of the
few fields it needs from the other service's payload.

All five services currently point at the same PostgreSQL database
(`movie_database`) but own separate tables and never read each other's.

Cross-service calls:

- user-service checks a location exists before creating a user.
- movie-service asks ticket-service which movies have an upcoming show in a
  city, and asks user-service for a reviewer's name when a rating is posted.
- ticket-service asks movie-service for a movie title and location-service for
  a theatre's address, and asks user-service for the owner of a ticket.

## Requirements

- Java 11 (the poms target 11; the Maven wrapper is committed, so no local Maven)
- PostgreSQL 12 or later
- Zookeeper 3.6 or later
- Docker, if you want to run the stack with compose

## Running it

With Docker, from the repository root:

```
docker compose up --build
```

That starts PostgreSQL, Zookeeper and the five services, and creates the
`movie_database` database on first boot.

To run a single service against a local database and Zookeeper:

```
createdb movie_database
cd ticket-service
./mvnw spring-boot:run
```

Hibernate is on `ddl-auto=update`, so the tables are created on first start.
Nothing seeds data: create a location, then a theatre and its seats, then a
movie, then a show, before you can book a ticket.

Tests are plain JUnit and Mockito over the service layer and need no database:

```
./mvnw test
```

## Configuration

Every setting has a working default for local development and is overridable by
environment variable.

| Variable | Default | Applies to |
|---|---|---|
| `SERVER_PORT` | per service, see the table above | all |
| `DB_URL` | `jdbc:postgresql://localhost:5432/movie_database` | all |
| `DB_USERNAME` / `DB_PASSWORD` | `postgres` / `postgres` | all |
| `DB_POOL_SIZE` / `DB_POOL_MIN_IDLE` | `20` / `5` | all |
| `ZOOKEEPER_CONNECT_STRING` | `localhost:2181` | all |
| `DISCOVERY_ENABLED` | `true` | all |
| `DDL_AUTO` | `update` | all |
| `SHOW_SQL` | `false` | all |
| `REST_CONNECT_TIMEOUT_MS` / `REST_READ_TIMEOUT_MS` | `2000` / `5000` | user, movie, ticket |
| `IMAGE_STORAGE_DIR` | `images` | image |

Each service exposes `/actuator/health` and Swagger UI at `/swagger-ui.html`
(the OpenAPI document is at `/v3/api-docs`).

## API

List endpoints are paged and take the usual `page`, `size` and `sort` query
parameters. Errors come back as `{"timestamp", "status", "error", "message"}`.

### user-service

```
POST   /user-info-management/user           create a user
GET    /user-info-management/user           list users
GET    /user-info-management/user/{id}      one user
DELETE /user-info-management/user/{id}      delete a user
POST   /user-info-management/user/login     check a username and password
```

Passwords are stored bcrypt hashed and are never returned.

### location-service

```
POST   /location-info-management/location             create a location
GET    /location-info-management/location             list locations
GET    /location-info-management/location/city/{city} locations in a city
GET    /location-info-management/location/{id}        one location
DELETE /location-info-management/location/{id}        delete a location
```

### movie_service

```
POST   /movie-info-management/movie              add a movie
GET    /movie-info-management/movie              list movies
GET    /movie-info-management/movie/city/{city}  movies playing in a city
GET    /movie-info-management/movie/{id}         one movie with its ratings
GET    /movie-info-management/movie/{id}/{city}  one movie plus whether it is playing there
DELETE /movie-info-management/movie/{id}         delete a movie
POST   /movie-info-management/movie/filter?city= filter by genre and language

POST   /movie-rating-management/rating           rate a movie
GET    /movie-rating-management/rating           list ratings
GET    /movie-rating-management/rating/movie/{id} ratings for a movie
GET    /movie-rating-management/rating/user/{id}  ratings by a user
```

A user can rate a movie once; posting a second rating returns 409. The movie's
average is recalculated on every new rating.

### ticket-service

```
POST   /theatre-info-management/theatre                add a theatre
GET    /theatre-info-management/theatre                list theatres
GET    /theatre-info-management/theatre/{id}           one theatre
DELETE /theatre-info-management/theatre/{id}           delete a theatre
GET    /theatre-info-management/theatre/movie/{movieId}?date=&city=
                                                       theatres in a city showing a movie on a date

POST   /seat-info-management/seats                     add seats, as a list
GET    /seat-info-management/seats/{id}                one seat
GET    /seat-info-management/seats/theatre/{theatreId} seats in a theatre
DELETE /seat-info-management/seats/{id}                delete a seat

POST   /show-info-management/show                      create a show
GET    /show-info-management/show                      list shows
GET    /show-info-management/show/{id}                 one show
DELETE /show-info-management/show/{id}                 delete a show
GET    /show-info-management/show/{showId}/seats       seat map with availability
GET    /show-info-management/show/movie/{movieId}?city= upcoming shows for a movie
GET    /show-info-management/show/available-movies?city= ids of movies with an upcoming show

POST   /ticket-management/ticket                       book seats
GET    /ticket-management/ticket/{id}                  one ticket
PUT    /ticket-management/ticket/cancel/{id}           cancel a ticket
GET    /ticket-management/ticket/user/{userId}         a user's tickets
GET    /ticket-management/ticket/show/{showId}         tickets for a show
```

Booking prices gold seats at the show price and diamond seats at 1.5x. Seats
already held by an upcoming ticket are rejected with 409. A scheduled job moves
tickets to `COMPLETED` once their show has started; cancelling is only allowed
while a ticket is `UPCOMING`.

The seat lock taken during booking is in-process, so running more than one
instance of ticket-service needs a database constraint or a distributed lock to
close the double booking window.

### image-service

```
POST /image-info-management/image/upload   multipart upload, field name "file"
GET  /image-info-management/image          list stored images
GET  /image-info-management/image/{id}     the image bytes
```

Files go to the directory named by `IMAGE_STORAGE_DIR` and are served with a
30 day `Cache-Control`. Movies reference an image by the id returned from the
upload.
