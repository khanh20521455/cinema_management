package cinema_management.controller;

import java.util.List;

import cinema_management.entities.Movie;
import cinema_management.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cinema_management.repository.MovieticketRepository;
import cinema_management.entities.Movieticket;


@RestController
public class SearchController {


    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchByName(@PathVariable("query") String query){
        List<Movie> movieList= movieRepository.findByNameContaining(query);
        return ResponseEntity.ok(movieList);

    }
}
