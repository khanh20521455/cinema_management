package cinema_management.service;

import cinema_management.entities.*;
import cinema_management.helper.Message;
import cinema_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ShowtimesRepository showtimesRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TheaterRepository theaterRepository;
    public String buyTicket(Integer id, Model model){
        Booking booking=new Booking();
        model.addAttribute("booking", booking);
        Movie movie= this.movieRepository.getById(id);
        model.addAttribute("movie", movie);
        List<Showtimes> showtimesList= this.showtimesRepository.findByMovieId(id);
        model.addAttribute("showtimesList", showtimesList);
        return "normaluser/booking/buy_ticket";
    }
    public String buyTicketProcess(Booking booking, Integer movieId,
                                   Principal principal, Model model, HttpSession session) {
        Movie movie=this.movieRepository.getById(movieId);
        model.addAttribute("movie", movie);

        try {
            User user = this.userRepository.getUserByUserName(principal.getName());
            booking.setUser(user);
            booking.setStatus(0);
            booking.setTotal(booking.getNumberOfSeat()*booking.getShowtimes().getPrice());
            booking.setActualTotal(booking.getNumberOfSeat()*booking.getShowtimes().getPrice());
            this.bookingRepository.save(booking);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Đã xảy ra lỗi, vui lòng thử lại ! ", "danger"));
        }
        int total= booking.getNumberOfSeat()*booking.getShowtimes().getPrice();
        model.addAttribute("total",total);
        model.addAttribute("booking",booking);
        return "normaluser/booking/confirm_buy_ticket";
    }
    

    public String confirmTicket(Integer id, Booking booking, Model model,HttpSession session){
        Optional<Booking> notConfirmOptional= this.bookingRepository.findById(id);
        Booking oldbooking=notConfirmOptional.get();
        try {
            oldbooking.setStatus(1);
            this.bookingRepository.save(oldbooking);
            for(int i=0;i<oldbooking.getNumberOfSeat();i++){
                Integer showtimeId=oldbooking.getShowtimes().getId();
                Integer seatNumber=oldbooking.getSeatList()[i];
                Seat seat= this.seatRepository.choosingSeat(showtimeId,seatNumber);
                seat.setStatus(1);
                this.seatRepository.save(seat);
            }
            session.setAttribute("message", new Message("Đặt vé thành công, chờ xác nhận!", "success"));
        }
        catch (Exception e){
            session.setAttribute("message", new Message("Đã xảy ra lỗi, vu lòng thử lại " + e.getMessage(), "danger"));
        }
        return movieService.homeScreen(model);
    }
    public String getTheaterForBookingManagement(Integer page, Model model){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Theater> theaterList = theaterRepository.findAllOrderByNameAsc(pageable);
        model.addAttribute("theaterList",theaterList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", theaterList.getTotalPages());
        return "adminuser/booking/theater_screen";
    }
    public String getBookingWaitConfirm(Integer theaterId, Integer page, Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Booking> bookingList= bookingRepository.bookingWaitConfirm(theaterId, pageable);
        model.addAttribute("bookingList", bookingList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookingList.getTotalPages());
        return "adminuser/booking/booking_management";
    }
    public String confirmBooking(Integer id){
        Booking booking= this.bookingRepository.getById(id);
        booking.setStatus(2);
        booking.setCompletedAt(new Date(System.currentTimeMillis()));
        bookingRepository.save(booking);
        Integer theaterId = booking.getShowtimes().getRoom().getTheater().getId();
        return "redirect:/admin/booking_management/"+theaterId+"/0";
    }
    public String cancelBooking(Integer id){
        Booking booking= this.bookingRepository.getById(id);
        booking.setStatus(3);
        bookingRepository.save(booking);
        for(int i=0;i<booking.getNumberOfSeat();i++){
            Integer showtimeId=booking.getShowtimes().getId();
            Integer seatNumber=booking.getSeatList()[i];
            Seat seat= this.seatRepository.choosingSeat(showtimeId,seatNumber);
            seat.setStatus(0);
            this.seatRepository.save(seat);
        }
        Integer theaterId = booking.getShowtimes().getRoom().getTheater().getId();
        return  "redirect:/admin/booking_management/"+theaterId+"/0";
    }
    public String getMovieWatched(Principal principal, Model model, Integer page){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Booking> bookingList= bookingRepository.findFirstByStatusAndShowtimesDateLessThanEqualAndUserEmail(new Date(System.currentTimeMillis()),principal.getName(),pageable);
        model.addAttribute("bookingList", bookingList);
        for(Booking booking: bookingList){
            if(this.commentRepository.commentExist(booking.getUser().getEmail(),booking.getShowtimes().getMovie().getId())!=null){
                booking.setStatusComment(1);
            }
            else  booking.setStatusComment(0);
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookingList.getTotalPages());
        return "normaluser/movie_watched";
    }

    public String getHistoryTransaction(Integer page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Booking> bookingList= bookingRepository.bookingHistory(principal.getName(),pageable);
        model.addAttribute("bookingList", bookingList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookingList.getTotalPages());
        return "normaluser/history_transaction";
    }
    public String cancelBookingUser(Integer id){
        Booking booking= this.bookingRepository.getById(id);
        booking.setStatus(3);
        bookingRepository.save(booking);
        for(int i=0;i<booking.getNumberOfSeat();i++){
            Integer showtimeId=booking.getShowtimes().getId();
            Integer seatNumber=booking.getSeatList()[i];
            Seat seat= this.seatRepository.choosingSeat(showtimeId,seatNumber);
            seat.setStatus(0);
            this.seatRepository.save(seat);
        }
        return "redirect:/user/history_transaction/0";
    }
}
