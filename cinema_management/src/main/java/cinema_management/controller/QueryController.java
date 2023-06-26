package cinema_management.controller;

import cinema_management.entities.*;
import cinema_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
public class QueryController {
    @Autowired
    private ShowtimesRepository showtimesRepository;
    @Autowired
    private SeatRepository seatRepository;
    @Autowired
    private BookingRepository bookingRepository;
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
    @GetMapping("/query")
    public List<Room> getTypeScreen(@RequestParam("id") Integer id, @RequestParam("date") Date date){
        List<Room> roomList=new ArrayList<>();
        List<Showtimes> showtimesList=this.showtimesRepository.typeScreenShowtimes(id, date);
        for(Showtimes showtimes: showtimesList){
            roomList.add(showtimes.getRoom());
        }
        return roomList;
    }
    @GetMapping("/queryShowtimes")
    public List<Showtimes> getTime(@RequestParam("id") Integer id, @RequestParam("date") Date date, @RequestParam("typeScreen")String typeScreen){
        return this.showtimesRepository.timeShowtimes(id, date, typeScreen);
    }
    @GetMapping("/querySeatNotSelect")
    public  List<Seat> getSeatNotSelectYet(@RequestParam("id")Integer showtimesId){
        return this.seatRepository.seatNotSelectYet(showtimesId);
    }
    @GetMapping("/queryBooking")
    public Booking getBooking(@RequestParam("id") Integer id){
        return this.bookingRepository.getById(id);
    }
}
