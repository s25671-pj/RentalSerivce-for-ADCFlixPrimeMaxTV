package pl.pjatk.RentalService.Service;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pl.pjatk.RentalService.Movie.Movie;
import java.net.URI;

@Service
public class RentalService {


    private final RestTemplate restTemplate;

    public RentalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Movie getMovieById(Long id){
        String url = "http://localhost:8080/movies/" +id;
        return restTemplate.getForObject(URI.create(url), Movie.class);
    }

    public void makeAvailable(long id){
        String url = "http://localhost:8080/movies/ava/" +id;
        restTemplate.put(URI.create(url), Movie.class);
    }

    public void makeNotAvailable(long id){
        String url = "http://localhost:8080/movies/nava/" +id;
        restTemplate.put(URI.create(url), Movie.class);
    }
    public void checkError(Long movieId) {
        String movieServiceUrl = "https://movieservice.example.com/movies/" + movieId; // Zastąp adresem API MovieService

        try {
            ResponseEntity<Movie> response = restTemplate.exchange(movieServiceUrl, HttpMethod.GET, null, Movie.class);
            HttpStatus statusCode = (HttpStatus) response.getStatusCode();

            if (statusCode.is2xxSuccessful()) {
                Movie movie = response.getBody();
                ResponseEntity.ok(movie);
            } else {
                handleError(statusCode);
            }
        } catch (HttpStatusCodeException ex) {
            HttpStatus statusCode = (HttpStatus) ex.getStatusCode();
            handleError(statusCode);
        } catch (ResourceAccessException ex) {
            handleError(HttpStatus.GATEWAY_TIMEOUT);
        }
    }

    private void handleError(HttpStatus statusCode) {
        if (statusCode == HttpStatus.NOT_FOUND) {
            System.out.println("Movie not found");
            ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (statusCode == HttpStatus.BAD_REQUEST) {
            System.out.println("Invalid request");
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR) {
            System.out.println("Movie service internal server error");
            ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        } else {
            System.out.println("Movie service call failed with status code: " + statusCode.value());
            ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

}