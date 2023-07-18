package cinema_management.controller;

import cinema_management.entities.*;
import cinema_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
    @GetMapping("/bookingForStatistic_ticket_asc")
    public  List<Object[]> getBookingForStatisticTicketASC(@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticTicketASC(start, end);
    }
    @GetMapping("/bookingForStatistic_revenue_asc")
    public  List<Object[]> getBookingForStatisticRevenueASC(@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticRevenueASC(start, end);
    }
    @GetMapping("/bookingForStatistic_ticket_desc")
    public  List<Object[]> getBookingForStatisticTicketDESC(@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticTicketDESC(start, end);
    }
    @GetMapping("/bookingForStatistic_revenue_desc")
    public  List<Object[]> getBookingForStatisticRevenueDESC(@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticRevenueDESC(start, end);
    }
    @GetMapping("/ticketFerMonth")
    public int[] getTicketMonthly(){
        int[] result=new int[12];
        for (int i=0;i<12;i++){
            result[i]=0;
        }
        List<Object[]> statistic= this.bookingRepository.ticketPerMonth();
        for (Object[] objects: statistic){
            int index= (int)objects[0];
            result[index-1] =((Long) objects[1]).intValue();
        }
        return result;
    }
    @GetMapping("/revenueFerMonth")
    public int[] getRevenueMonthly(){
        int[] result=new int[12];
        for (int i=0;i<12;i++){
            result[i]=0;
        }
        List<Object[]> statistic= this.bookingRepository.revenuePerMonth();
        for (Object[] objects: statistic){
            int index= (int)objects[0];
            result[index-1] =((Long) objects[1]).intValue();
        }
        return result;
    }
    @GetMapping("/ticketOfYear")
    public int getTicketYear(){
        Date current=new Date(System.currentTimeMillis());
        int year = current.toLocalDate().getYear();
        return this.bookingRepository.ticketOfYear(year);
    }
    @GetMapping("/revenueOfYear")
    public long getRevenueOfYear(){
        Date current=new Date(System.currentTimeMillis());
        return this.bookingRepository.revenueOfYear(current.toLocalDate().getYear());
    }
    @GetMapping("/ticketOfToday")
    public int getTicketToday(){
        Date current=new Date(System.currentTimeMillis());
        return this.bookingRepository.ticketOfToday(current);
    }
    @GetMapping("/revenueOfToday")
    public long getRevenueOfToday(){
        Date current=new Date(System.currentTimeMillis());
        return this.bookingRepository.revenueOfToday(current);
    }
}
