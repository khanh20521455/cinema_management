package cinema_management.service;

import cinema_management.entities.*;
import cinema_management.helper.Message;
import cinema_management.repository.*;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ShowtimesRepository showtimesRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private CommentRepository commentRepository;
    public  String[] genreList= {"Hành động", "Tài liệu","Tình cảm", "Hài hước","Hoạt hình", "Khoa học viễn tưởng","Kinh dị", "Tội phạm", "Phiêu lưu", "Thần thoại"};
    //Management
    public String getMovieManagement(Integer page, Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Movie> movieList = movieRepository.findAllOrderByMovieDateAsc(pageable);
        model.addAttribute("movieList", movieList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", movieList.getTotalPages());
        return "adminuser/movie/movie_management";
    }

    public String addMovie(Model model) {
        model.addAttribute("genreList",genreList);
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

            session.setAttribute("message", new Message("Thêm phim mới thành công", "success"));
            movieRepository.save(movie);
        }
        catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Đã xảy ra lỗi, vui lòng thử lại ! ", "danger"));
        }

        return "redirect:/admin/movie_management/0";

    }

    public String updateMovie(Integer id, Model m) {

        Optional<Movie> movieOptional = this.movieRepository.findById(id);
        Movie movie = movieOptional.get();
        m.addAttribute("genreList",genreList);
        m.addAttribute("movie", movie);
        return "adminuser/movie/update_movie";
    }

    public String movieUpdateProcess(
            Integer id,
            Movie movie,
            MultipartFile file,
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

            session.setAttribute("message", new Message("Cập nhật phim thành công", "success"));
        } catch (Exception e) {
            e.printStackTrace();

            m.addAttribute("Title", "Update Movie");
            m.addAttribute("movieticket", oldMovieDetail);
            session.setAttribute("message", new Message("Đã xảy ra lỗi " + e.getMessage(), "danger"));
        }


        m.addAttribute("Title", "Update Movie");
        m.addAttribute("movie", movie);
        return "redirect:/admin/movie_management/0";
    }
    public String deleteMovie(Integer id){
        List<Showtimes> showtimesList= this.showtimesRepository.ShowtimesBaseOnMovie(id);
        for(Showtimes showtimes:showtimesList){
            List<Booking> bookingList=this.bookingRepository.bookingBaseShowtimes(showtimes.getId());
            for (Booking booking: bookingList){
                this.bookingRepository.deleteById(booking.getId());
            }
            List<Seat> seatList=this.seatRepository.seatBaseShowtimes(showtimes.getId());
            for (Seat seat: seatList){
                this.seatRepository.deleteById(seat.getId());
            }
            this.showtimesRepository.deleteById(showtimes.getId());
        }
        movieRepository.deleteById(id);
        return "redirect:/admin/movie_management/0";
    }

    //User
    public String movieDetail(Integer id, Model model){
        Optional<Movie> movieOptional = this.movieRepository.findById(id);
        Movie movie = movieOptional.get();
        List<Comment> commentList=this.commentRepository.commentMovie(id);
        model.addAttribute("commentList",commentList);
        int sum = 0;
        int dem=0;
        for (Comment comment: commentList){
            sum += comment.getRating();
            dem++;
        }
        Double avg= ((double) sum)/((double) dem);
        String numberString = Double.toString(avg);
        double roundedX = Math.round(avg * 10.0) / 10.0;
        model.addAttribute("avg",roundedX);
        model.addAttribute("movie", movie);
        return "normaluser/movie_detail";
    }
    public String homeScreen(Model model){
        Date now= new Date(System.currentTimeMillis());
        List<Movie> movieUpcomingList= movieRepository.movieUpcoming(now);
        model.addAttribute("movieUpcomingList", movieUpcomingList);
        List<Movie> moviePlayingList= movieRepository.moviePlaying(now);
        model.addAttribute("moviePlayingList", moviePlayingList);
        return "normaluser/home";
    }
}
