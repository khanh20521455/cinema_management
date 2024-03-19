package cinema_management.service;

import cinema_management.entities.*;
import cinema_management.helper.Message;
import cinema_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
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
import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
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
    @Autowired
    private UserRepository userRepository;
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


    //User
    public String movieDetail(Principal principal,Integer id, Model model){
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
        Double avg= (double)0;
        if(dem > 0){
            avg = ((double) sum)/((double) dem);
            double roundedX = Math.round(avg * 10.0) / 10.0;
            model.addAttribute("avg",roundedX);
        }
        else
            model.addAttribute("avg",0);
        User user =  this.userRepository.getUserByUserName(principal.getName());
        if(user.getFavorites().contains(movie))
        {
            model.addAttribute("isF",1);
        }
        else
            model.addAttribute("isF",0);
        model.addAttribute("movie", movie);
        return "normaluser/booking/movie_detail";
    }
    public String addFavoriteList(Principal principal, Integer id, Model model){
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
        Double avg= (double)0;
        if(dem > 0){
            avg = ((double) sum)/((double) dem);
            double roundedX = Math.round(avg * 10.0) / 10.0;
            model.addAttribute("avg",roundedX);
        }
        else
            model.addAttribute("avg",0);
        User user =  this.userRepository.getUserByUserName(principal.getName());
        user.getFavorites().add(movie);
        userRepository.save(user);
        model.addAttribute("movie", movie);
        model.addAttribute("isF",1);
        return "normaluser/booking/movie_detail";
    }
    public String removeFavoriteList(Principal principal, Integer id, Model model){
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
        Double avg= (double) 0;
        if(dem > 0){
            avg = ((double) sum)/((double) dem);
            double roundedX = Math.round(avg * 10.0) / 10.0;
            model.addAttribute("avg",roundedX);
        }
        else
            model.addAttribute("avg",0);
        User user =  this.userRepository.getUserByUserName(principal.getName());
        user.getFavorites().remove(movie);
        userRepository.save(user);
        model.addAttribute("movie", movie);
        model.addAttribute("isF",0);
        return "normaluser/booking/movie_detail";
    }
    public String homeScreen(Model model){
        Date now= new Date(System.currentTimeMillis());
        Pageable pageable = PageRequest.of(0, 3);
        List<Movie> maybeLike = bookingRepository.listMovieBestSale(now,pageable);
        List<Movie> movieBestRating = commentRepository.findMoviesByAverageRating(now, pageable);
        for(Movie m : movieBestRating){
            if(!maybeLike.contains(m))
                maybeLike.add(m);
        }
        model.addAttribute("maybeLike", maybeLike);
        List<Movie> movieUpcomingList= movieRepository.movieUpcoming(now);
        model.addAttribute("movieUpcomingList", movieUpcomingList);
        List<Movie> moviePlayingList= movieRepository.moviePlaying(now);
        model.addAttribute("moviePlayingList", moviePlayingList);
        return "normaluser/home";
    }
}
