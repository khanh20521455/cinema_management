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
    private MovieticketRepository movieticketRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;
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


        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong. " + e.getMessage(), "danger"));

        }
        return "normaluser/buy_ticket";
    }
//    @PostMapping
//    public String searchMoviesByName(HttpServletRequest request, Model model){
//        // Gọi api lấy ra lịch được chọn
//        String urlTemplate = UriComponentsBuilder.fromHttpUrl(API_GET_SHOWING_MOVIES_BY_NAME)
//                .queryParam("name", "{name}")
//                .encode()
//                .toUriString();
//        Map<String,String> listRequestParam = new HashMap<>();
//        listRequestParam.put("name", request.getParameter("movie-name"));
//        ResponseEntity<MovieDTO[]> response = restTemplate.getForEntity(urlTemplate,MovieDTO[].class,listRequestParam);
//        MovieDTO[] movies = response.getBody();
//        if(movies.length==0){
//            return "movie-not-found";
//        }
//        model.addAttribute("movies",movies);
//        model.addAttribute("user",new User());
//        return "home";
//    }
//}



    // show upcoming movies Normal User view




    @RequestMapping("/buy-movie-ticket/{id}")
    public String buyMovieTicket(@PathVariable("id") Integer id, Model model) {

        Movieticket movieticket = this.movieticketRepository.getById(id);
        model.addAttribute("title", movieticket.getMovieName() + " Buy Movie Ticket");
        model.addAttribute("movieticket", movieticket);

        return "normaluser/buy_movie_ticket";
    }

    // process buy movie ticket : update database

    @PostMapping("/process-buy-ticket/{id}")
    public String processBuyTicket(@ModelAttribute Purchase purchase, @PathVariable("id") Integer id,
                                   Principal principal, Model model, HttpSession session) {

        Movieticket movieticket = this.movieticketRepository.getById(id);
        model.addAttribute("title", movieticket.getMovieName() + " Buy Movie Ticket");
        model.addAttribute("movieticket", movieticket);

        try {

            if (purchase.getQuantity() <= 0) {
                throw new Exception("No seat has been selected.");
            }

            if (movieticket.getSeatRemaining() - purchase.getQuantity() < 0) {
                throw new Exception("We don't have enough seats.");
            }

            movieticket.setSeatRemaining(movieticket.getSeatRemaining() - purchase.getQuantity());

            String userName = principal.getName();
            User user = this.userRepository.getUserByUserName(userName);

            purchase.setUser(user);
            purchase.setMovieticket(movieticket);

            user.getPurchaseList().add(purchase);
            movieticket.getSoldList().add(purchase);

            purchaseRepository.save(purchase);
            session.setAttribute("message", new Message("Ticket successfully purchased.", "success"));

            return "normaluser/buy_movie_ticket";

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong. " + e.getMessage(), "danger"));
            return "normaluser/buy_movie_ticket";
        }

    }



    // show upcoming movies in user's purchase list


    @GetMapping("/show-user-purchase/{page}")
    public String showUserPurchase(@PathVariable("page") Integer page, Model m, Principal principal) {

        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);

        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        int status = 1;

        Pageable pageable = PageRequest.of(page, 5);
        Page<Purchase> purchaseList = purchaseRepository.getPurchaseByUserAndMovieDateAndPaymentStatus(user.getUserId(), date, status, pageable );

        m.addAttribute("title", "My Movie Watchlist");
        m.addAttribute("purchaseList", purchaseList);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", purchaseList.getTotalPages());

        System.out.println(purchaseList.getTotalPages());

        return "normaluser/show_user_purchase";

    }



    // show user transaction history


    @GetMapping("/show-user-transaction/{page}")
    public String showUserTransaction(@PathVariable("page") Integer page, Model m, Principal principal) {

        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);

        Pageable pageable = PageRequest.of(page, 5);
        Page<Purchase> purchaseList = purchaseRepository.getPurchaseByUser(user.getUserId(),pageable );

        m.addAttribute("title", "My Movie Watchlist");
        m.addAttribute("purchaseList", purchaseList);
        m.addAttribute("currentPage", page);
        m.addAttribute("totalPages", purchaseList.getTotalPages());


        return "normaluser/show_user_transaction";

    }


    // user profile

    @GetMapping("/user-profile")
    public String userProfile(Model model) {
        model.addAttribute("Title", "Profile Page");
        return "normaluser/user_profile";
    }


    // refund ticket

    @GetMapping("/user-refund-ticket/{id}")
    public String userRefundTicket(@PathVariable("id") Integer id, Model model, Principal principal) {

        Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
        Purchase purchase = purchaseOptional.get();

        Optional<Movieticket> movieticketOptional = movieticketRepository.findById( purchase.getMovieticket().getMovieId());
        Movieticket movieticket = movieticketOptional.get();

        model.addAttribute("title", "Refund Movie Ticket");
        model.addAttribute("movieticket", movieticket);
        model.addAttribute("purchase",purchase);

        return "normaluser/user_refund_ticket";
    }



    // process refund movie ticket

    @PostMapping("/process-refund-ticket/{id}")
    public String processRefundTicket(@ModelAttribute Purchase purchase,
                                      @PathVariable("id") Integer id,
                                      Principal principal, Model model,
                                      HttpSession session) {


        Optional<Purchase> purchaseOptional = purchaseRepository.findById(id);
        Purchase oldPurchase = purchaseOptional.get();

        Optional<Movieticket> movieticketOptional = movieticketRepository.findById( oldPurchase.getMovieticket().getMovieId());
        Movieticket movieticket = movieticketOptional.get();

        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);

        try {


            if(purchase.getQuantity()<=0) {
                throw new Exception("Invalid Input.");
            }

            else if( oldPurchase.getQuantity() < purchase.getQuantity() ) {
                throw new Exception("You have not purchased that many tickets.");

            }

            else {

                int curQuantity = oldPurchase.getQuantity() - purchase.getQuantity();
                int refundMoney = purchase.getQuantity() * movieticket.getTicketPrize();
                oldPurchase.setQuantity(curQuantity);
                user.setRefundAmount(refundMoney + user.getRefundAmount());
                movieticket.setSeatRemaining(purchase.getQuantity()+movieticket.getSeatRemaining());
                userRepository.save(user);
                movieticketRepository.save(movieticket);

                if(oldPurchase.getQuantity()==0) {
                    purchaseRepository.deleteById(oldPurchase.getPurchaseId());
                    session.setAttribute("message", new Message("Ticket has been successfully refunded.", "success"));
                    return "redirect:/user/show-user-purchase/0";
                }

                session.setAttribute("message", new Message("Ticket has been successfully refunded.", "success"));
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong. " + e.getMessage(), "danger"));
        }


        return "redirect:/user/user-refund-ticket/"+oldPurchase.getPurchaseId();
    }



}

