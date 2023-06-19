package cinema_management.controller;

import java.sql.Date;
import java.util.List;

import cinema_management.entities.Movie;
import cinema_management.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SearchController {


    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/search/{query}")
    public List<Movie> searchByName(@PathVariable("query") String query){
       return this.movieRepository.findByNameContainingAndEndAfter(query,new Date(System.currentTimeMillis()));
    }
    @GetMapping("/search_admin/{query}")
    public  List<Movie> searchByNameAdmin(@PathVariable("query") String query){
        return this.movieRepository.findByNameContaining(query);
    }
}
