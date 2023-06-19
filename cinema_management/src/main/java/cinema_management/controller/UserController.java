package cinema_management.controller;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import cinema_management.entities.*;
import cinema_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cinema_management.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ShowtimesRepository showtimesRepository;

    // adding common data

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user", user);
    }

    // user dashboard

    @RequestMapping("/dashboard")
    public String userDashboard(Model model, Principal principal) {

        model.addAttribute("title", "User Dashboard");
        return "normaluser/user_dashboard";
    }
    @GetMapping("/home")
    public String homeScreen(Model model){
        Date now= new Date(System.currentTimeMillis());
        List<Movie> movieUpcomingList= movieRepository.movieUpcoming(now);
        model.addAttribute("movieUpcomingList", movieUpcomingList);
        List<Movie> moviePlayingList= movieRepository.moviePlaying(now);
        model.addAttribute("moviePlayingList", moviePlayingList);
        return "normaluser/home";
    }
    @GetMapping("movie_detail/{id}")
    public String movieDetail(@PathVariable("id") Integer id, Model model){
        Optional<Movie> movieOptional = this.movieRepository.findById(id);
        Movie movie = movieOptional.get();
        model.addAttribute("movie", movie);
        return "normaluser/movie_detail";
    }
    @RequestMapping("buy_ticket/{id}")
    public String buyTicket(@PathVariable("id") Integer id, Model model){
        Booking booking=new Booking();
        model.addAttribute("booking", booking);
        Movie movie= this.movieRepository.getById(id);
        model.addAttribute("movie", movie);
        List<Showtimes> showtimesList= this.showtimesRepository.findByMovieId(id);
        model.addAttribute("showtimesList", showtimesList);
        return "normaluser/buy_ticket";
    }
    @PostMapping("/buy_ticket_process/{id}")
    public String buyTicketProcess(@ModelAttribute Booking booking, @PathVariable("id") Integer id,
                                   Principal principal, Model model, HttpSession session) {

        Movie movie=this.movieRepository.getById(id);
        model.addAttribute("movie", movie);

        try {
            String userName = principal.getName();
            User user = this.userRepository.getUserByUserName(userName);
            booking.setUser(user);
            booking.setStatus(0);
            user.getBookingList().add(booking);
            bookingRepository.save(booking);
            session.setAttribute("message", new Message("New Movie has been successfully uploaded", "success"));

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong. " + e.getMessage(), "danger"));

        }
        return "normaluser/buy_ticket";
    }



}

