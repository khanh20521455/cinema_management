package cinema_management.controller;

import java.security.Principal;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import cinema_management.entities.*;
import cinema_management.repository.*;
import cinema_management.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private RoomService roomService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private ShowtimesService showtimesService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TheaterService theaterService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SnacksService snacksService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private TheaterRepository theaterRepository;
    // adding common data
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user", user);
    }
    @GetMapping("/admin_profile")
    public String userProfile(Model model, Principal principal) {
        User user= this.userRepository.getUserByUserName(principal.getName());
        model.addAttribute("user", user);
        return "adminuser/admin_profile";
    }

    //Room management
    @GetMapping("/theater_room_management/{page}")
    public String getTheaterForRoomManagement(@PathVariable("page") Integer page, Model model){
        return this.roomService.getTheaterForRoomManagement(page,model);
    }
    @GetMapping("/room_management/{id}/{page}")
    public String getRoom(@PathVariable("page") Integer page, @PathVariable("id") Integer id, Model model){
        return roomService.getRoomManagement(page, model, id);
    }
    @GetMapping("/add_room/{id}")
    public String addRoom(Model model,@PathVariable("id") Integer theaterId){
        return roomService.addRoom(model, theaterId);
    }
    @PostMapping("/add_room_process/{id}")
    public String addRoomProcess(@ModelAttribute Room room, HttpSession session, @PathVariable("id") Integer theaterId){
        return roomService.addRoomProcess(room,session, theaterId);

    }
    @GetMapping("/update_room/{id}")
    public String updateRoom(@PathVariable("id") Integer id, Model model) {
        return roomService.updateRoom(id,model);
    }
    @PostMapping("update_room_process/{id}")
    public String movieUpdateProcess(
            @PathVariable("id") Integer id,
            @ModelAttribute Room room,
            Model model, HttpSession session) {
        return roomService.roomUpdateProcess(id,room, model,session);
    }
    @GetMapping("/delete_room/{id}")
    public String deleteRoom(@PathVariable("id") Integer id){
        return roomService.deleteRoom(id);
    }

    //Movie management
    @GetMapping("/movie_management/{page}")
    public String getMovie(@PathVariable("page") Integer page, Model model){
        return movieService.getMovieManagement(page,model);
    }
    @GetMapping("/add_movie")
    public String uploadMovie(Model model) {
        return movieService.addMovie(model);
    }
    @PostMapping("/add_movie_process")
    public String processMovie(
            @ModelAttribute Movie movie,
            @RequestParam("movieImageUrl") MultipartFile file,
            HttpSession session) {
        return movieService.addMovieProcess(movie, file,session);
    }
    @GetMapping("/update_movie/{id}")
    public String updateMovie(@PathVariable("id") Integer id, Model model){
        return movieService.updateMovie(id, model);
    }
    @PostMapping("/update_movie_process/{id}")
    public String updateMovieProcess(@PathVariable("id") Integer id,
                                     @ModelAttribute Movie movie,
                                     @RequestParam("movieImageUrl") MultipartFile file,
                                     Model model, HttpSession session){
        return movieService.movieUpdateProcess(id, movie,file,model,session);
    }
    @GetMapping("/delete_movie/{id}")
    public String deleteMovie(@PathVariable("id") Integer id){
        return movieService.deleteMovie(id);
    }

    //Showtimes management
    @GetMapping("/theater_showtimes_management/{page}")
    public String getTheaterForShowtimeManagement(@PathVariable("page") Integer page, Model model){
        return this.showtimesService.getTheaterForShowtimeManagement(page,model);
    }
    @GetMapping("/showtimes_management/{id}/{page}")
    public String showtimesManagement( @PathVariable("id")Integer id, @PathVariable("page") Integer page, Model model){
        return showtimesService.showtimesManagement(id,page,model);
    }
    @GetMapping("/add_showtimes/{id}")
    public String addShowtimes(@PathVariable("id") Integer id, Model model){
        return showtimesService.addShowtimes(id, model);
    }
    @PostMapping("/add_showtimes_process/{id}")
    public String addShowtimesProcess(@PathVariable("id")Integer theaterId,@ModelAttribute Showtimes showtimes, HttpSession session){
        return showtimesService.addShowtimesProcess(theaterId,showtimes,session);
    }
    @GetMapping("/update_showtimes/{id}")
    public String updateShowtimes(@PathVariable("id") Integer id, Model model){
        return showtimesService.updateShowtimes(id, model);
    }
    @PostMapping("/update_showtimes_process/{id}")
    public String updateShowtimesProcess(@PathVariable("id") Integer id,@ModelAttribute Showtimes showtimes, Model model,HttpSession session){
        return showtimesService.showtimesUpdateProcess(id, showtimes,model,session);
    }
    @GetMapping("/cancel_showtimes/{id}")
    public String deleteShowtimes(@PathVariable("id") Integer id){
        return showtimesService.cancelShowtimes(id);
    }

    //Booking management
    @GetMapping("/theater_booking_management/{page}")
    public String getTheaterScreen(@PathVariable("page") Integer page, Model model){
        return bookingService.getTheaterForBookingManagement(page, model);
    }
    @GetMapping("/booking_management/{id}/{page}")
    public String getBookingWaitConfirm(@PathVariable("id") Integer id, @PathVariable("page") Integer page,Model model){
        return bookingService.getBookingWaitConfirm(id, page,model);
    }
    @GetMapping("/confirm_booking/{id}")
    public String confirmBooking(@PathVariable("id") Integer id){
        return this.bookingService.confirmBooking(id);
    }
    @GetMapping("/cancel_booking/{id}")
    public String cancelBooking(@PathVariable("id") Integer id){
        return  this.bookingService.cancelBooking(id);
    }
    //Statistic
    @GetMapping("theater_statistic_management/{page}")
    public String getTheaterForStatistic(@PathVariable("page") Integer page, Model model){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Theater> theaterList = theaterRepository.findAllOrderByNameAsc(pageable);
        model.addAttribute("theaterList",theaterList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", theaterList.getTotalPages());
        return "adminuser/statistic/theater_screen";
    }
    @GetMapping("/statistic_chart")
    public String statisticYear(){
        return "adminuser/statistic/statistic_chart";
    }
    @GetMapping("/statistic_chart/{id}")
    public String statisticYearTheater(@PathVariable("id") Integer id, Model model){
        model.addAttribute("theaterId", id);
        return "adminuser/statistic/theater_statistic_chart";}
    @GetMapping("/comment_management/{page}")
    public String commentManagement(@PathVariable("page") Integer page, Model model){
        return this.commentService.getCommentWaitConfirm(page,model);
    }
    @GetMapping("/confirm_comment/{id}")
    public String confirm_comment(@PathVariable("id") Integer id)
    {
        return this.commentService.confirmComment(id);
    }
    @GetMapping("/cancel_comment/{id}")
    public String cancel_comment(@PathVariable("id") Integer id){
        return  this.commentService.cancelComment(id);
    }
    @GetMapping("/theater_management/{page}")
    public String theater_management(@PathVariable("page") Integer page, Model model){
        return this.theaterService.theaterManagement(model, page);
    }
    @GetMapping("/add_theater")
    public String addTheater(Model model){
        return theaterService.addTheater(model);
    }
    @PostMapping("/add_theater_process")
    public String addTheaterProcess(@ModelAttribute Theater theater, HttpSession httpSession){
        return theaterService.addTheaterProcess(theater, httpSession);
    }
    @GetMapping("/update_theater/{id}")
    public String updateTheater(@PathVariable("id") Integer id,Model model){
        return theaterService.updateTheater(id, model);
    }
    @PostMapping("/update_theater_process/{id}")
    public String updateTheaterProcess(@PathVariable("id") Integer id, @ModelAttribute Theater theater, HttpSession httpSession){
        return theaterService.updateTheaterProcess(id, theater,httpSession);
    }
    //Snack management
    @GetMapping("/snacks_management/{page}")
    public String getAll(@PathVariable("page") Integer page, Model model){
        return snacksService.getAll(page, model);
    }
    @GetMapping("/add_snacks")
    public  String addSnacks(Model model){
        return snacksService.addSnacks(model);
    }
    @PostMapping("/add_snacks_process")
    public  String addSnackProcess(@ModelAttribute Snacks snack, @RequestParam("imageUrl") MultipartFile file, HttpSession httpSession){
        return snacksService.addSnackProcess(snack, file, httpSession);
    }
    @GetMapping("/update_snacks/{id}")
    public  String updateSnacks(@PathVariable("id") Integer id, Model model){
        return snacksService.updateSnacks(id, model);
    }
    @PostMapping("/update_snacks_process/{id}")
    public String updateSnackProcess(@PathVariable("id") Integer id, @ModelAttribute Snacks snacks, @RequestParam("imageUrl") MultipartFile file, Model model, HttpSession httpSession){
        return snacksService.updateSnackProcess(id, snacks, file,  httpSession);
    }
    //Offer_management
    @GetMapping("/offer_management/{page}")
    public String getAllOffer (@PathVariable("page") Integer page, Model model){
        return offerService.getAll(page,model);
    }
    @GetMapping("/add_offer")
    public String addOffer(Model model){
        return offerService.addOffer(model);
    }
    @PostMapping("/add_offer_process")
    public String addOfferProcess(@ModelAttribute Offer offer, HttpSession session) throws Exception{
        return offerService.addOfferProcess(offer, session);
    }
    @GetMapping("/update_offer/{id}")
    public String updateOffer(@PathVariable("id") Integer id, Model model){
        return offerService.updateOffer(id, model);
    }
    @PostMapping("/update_offer_process/{id}")
    public String updateOfferProcess(@PathVariable("id") Integer id, @ModelAttribute Offer offer, HttpSession session){
        return offerService.updateOfferProcess(id, offer,session);
    }
}


