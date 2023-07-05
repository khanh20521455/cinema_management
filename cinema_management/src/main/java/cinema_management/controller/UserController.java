package cinema_management.controller;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cinema_management.entities.*;
import cinema_management.repository.*;
import cinema_management.service.BookingService;
import cinema_management.service.CommentService;
import cinema_management.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cinema_management.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepository;

    // adding common data
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user", user);
    }
    // user profile
    @GetMapping("/user-profile")
    public String userProfile(Model model, Principal principal) {
        User user= this.userRepository.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        return "normaluser/user_profile";
    }

    @GetMapping("/home")
    public String homeScreen(Model model){
        return movieService.homeScreen(model);
    }

    //Booking process
    @GetMapping("/movie_detail/{id}")
    public String movieDetail(@PathVariable("id") Integer id, Model model){
       return movieService.movieDetail(id,model);
    }
    @GetMapping("/buy_ticket/{id}")
    public String buyTicket(@PathVariable("id") Integer id, Model model){
        return bookingService.buyTicket(id,model);
    }
    @PostMapping("/buy_ticket_process/{movieId}")
    public String buyTicketProcess(@ModelAttribute Booking booking, @PathVariable("movieId") Integer movieId,
                                   Principal principal, Model model, HttpSession session) {
        return bookingService.buyTicketProcess(booking,movieId,principal,model,session);
    }
    @PostMapping("/confirm_booking/{id}")
    public String confirmBooking(@PathVariable("id") Integer id,@ModelAttribute Booking booking, Model model, HttpSession session){
        return this.bookingService.confirmTicket(id,booking,model,session);
    }
    @GetMapping("/movie_watched/{page}")
    public String movieWatched(Principal principal, Model model,@PathVariable("page") Integer page){
        return this.bookingService.getMovieWatched(principal,model,page);
    }

    //Comment
    @GetMapping("/add_comment/{movieId}")
    public String addComment(@PathVariable("movieId") Integer movieId, Principal principal, Model model){
        return commentService.addComment(movieId,principal,model);
    }
    @PostMapping("/add_comment_process/{id}")
    public String addCommentProcess(@PathVariable("id") Integer id,@ModelAttribute("comment") Comment comment, @RequestParam("rating") int rating){
        return this.commentService.addCommentProcess(id,comment,rating);
    }
    @GetMapping("/update_comment/{movieId}")
    public String updateComment(@PathVariable("movieId") Integer movieId, Principal principal, Model model){
        return commentService.updateComment(movieId,principal,model);
    }
    @PostMapping("/update_comment_process/{id}")
    public String updateCommentProcess(@PathVariable("id") Integer id,@ModelAttribute("comment") Comment comment, @RequestParam("rating") int rating){
        return this.commentService.updateCommentProcess(id,comment,rating);
    }
    @GetMapping("/history_transaction/{page}")
    public String getHistoryTransaction(@PathVariable("page") Integer page, Principal principal,Model model){
        return this.bookingService.getHistoryTransaction(page,principal,model);
    }
    @GetMapping("/cancel_booking/{id}")
    public String cancelBooking(@PathVariable("id") Integer id){
        return this.bookingService.cancelBookingUser(id);
    }
}

