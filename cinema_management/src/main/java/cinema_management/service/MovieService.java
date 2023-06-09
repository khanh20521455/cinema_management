package cinema_management.service;

import cinema_management.entities.Movie;
import cinema_management.helper.Message;
import cinema_management.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    public String getMovieManagement(Integer page, Model model) {
        Pageable pageable = PageRequest.of(page, 3);
        Page<Movie> movieList = movieRepository.findAllOrderByMovieDateAsc(pageable);
        model.addAttribute("movieList", movieList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", movieList.getTotalPages());
        return "adminuser/movie/movie_management";
    }

    public String addMovie(Model model) {
        model.addAttribute("movie", new Movie());
        return "adminuser/movie/add_movie";
    }
    public String addMovieProcess(
            Movie movie,
            MultipartFile file,
            HttpSession session) {
        try {

            if(file.isEmpty()) {
                movie.setPoster("Movie Poster Default Photo.png");
            }
            else {

                movie.setPoster(file.getOriginalFilename());
                File saveFile =  new ClassPathResource("static/img").getFile();
                Path path =   Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
            }

            session.setAttribute("message", new Message("New Movie has been successfully uploaded", "success"));
            movieRepository.save(movie);
        }
        catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong, try again ! ", "danger"));
        }

        return "adminuser/movie/add_movie";

    }

    public String updateMovie(Integer id, Model m) {

        Optional<Movie> movieOptional = this.movieRepository.findById(id);
        Movie movie = movieOptional.get();
        m.addAttribute("movie", movie);
        return "adminuser/movie/update_movie";
    }

    public String movieUpdateProcess(
            @PathVariable("id") Integer id,
            @ModelAttribute Movie movie,
            @RequestParam("movieImageUrl") MultipartFile file,
            Model m, HttpSession session) {


        Optional<Movie> movieOptional = movieRepository.findById(id);
        Movie oldMovieDetail = movieOptional.get();

        try {

            if (!file.isEmpty()) {

                //delete
                File deleteFile = new ClassPathResource("static/img").getFile();
                File file1 = new File(deleteFile, oldMovieDetail.getPoster());
                file1.delete();


                //update
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                movie.setPoster(file.getOriginalFilename());
            } else {
                movie.setPoster(oldMovieDetail.getPoster());
            }

            movie.setId(oldMovieDetail.getId());
            this.movieRepository.save(movie);

            session.setAttribute("message", new Message("Movie Updated Successfully.", "success"));
        } catch (Exception e) {
            e.printStackTrace();

            m.addAttribute("Title", "Update Movie");
            m.addAttribute("movieticket", oldMovieDetail);
            session.setAttribute("message", new Message("Something went wrong. " + e.getMessage(), "danger"));
        }


        m.addAttribute("Title", "Update Movie");
        m.addAttribute("movie", movie);
        return "adminuser/movie/update_movie";
    }
    public String deleteMovie(Integer id){
        movieRepository.deleteById(id);
        return "redirect:/admin/movie_management/0";
    }
}
