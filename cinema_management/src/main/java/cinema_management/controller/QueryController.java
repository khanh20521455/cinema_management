package cinema_management.controller;

import cinema_management.entities.*;
import cinema_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.round;

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
    @Autowired
    private TheaterRepository theaterRepository;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/search/{query}")
    public List<Movie> searchByName(@PathVariable("query") String query){
        List<Movie> movieList = this.movieRepository.findByNameContainingAndEndAfter(query, new Date(System.currentTimeMillis()));
        List<Movie> m1List= this.movieRepository.findByActorContainingAndEndAfter(query, new Date(System.currentTimeMillis()));
        List<Movie> m2List= this.movieRepository.findByDirectorContainingAndEndAfter(query, new Date(System.currentTimeMillis()));
        for (Movie m : m1List){
            if(!movieList.contains(m))
                movieList.add(m);
        }
        for (Movie m : m2List){
            if(!movieList.contains(m))
                movieList.add(m);
        }
        return movieList;
    }
    @GetMapping("/search_admin/{query}")
    public  List<Movie> searchByNameAdmin(@PathVariable("query") String query){
        List<Movie> movieList = this.movieRepository.findByNameContaining(query);
        List<Movie> m1List= this.movieRepository.findByActorContaining(query);
        List<Movie> m2List= this.movieRepository.findByDirectorContaining(query);
        for (Movie m : m1List){
            if(!movieList.contains(m))
                movieList.add(m);
        }
        for (Movie m : m2List){
            if(!movieList.contains(m))
                movieList.add(m);
        }
        return movieList;
    }
    @GetMapping("/queryProvince")
    public List<String> listProvince(){
        return this.theaterRepository.listProvince();
    }
    @GetMapping("/queryTheater")
    public List<Theater> getTheaterList(@RequestParam("id")Integer id, @RequestParam("province") String province){
        return this.showtimesRepository.theaterShowtimes(id, province);
    }
    @GetMapping("/queryTypeScreen")
    public List<String> getTypeScreen(@RequestParam("id") Integer id,@RequestParam("theaterId") Integer theaterId, @RequestParam("date") Date date){
        return this.showtimesRepository.typeScreenShowtimes(id, theaterId, date);
    }
    @GetMapping("/queryShowtimes")
    public List<Showtimes> getTime(@RequestParam("id") Integer id,@RequestParam("theaterId") Integer theaterId, @RequestParam("date") Date date, @RequestParam("typeScreen")String typeScreen){
        return this.showtimesRepository.timeShowtimes(id, theaterId, date, typeScreen);
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
    @GetMapping("/bookingForStatistic_cancel_asc")
    public  List<Object[]> getBookingForStatisticCancelASC(@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticCancelASC(start, end);
    }
    @GetMapping("/bookingForStatistic_ticket_desc")
    public  List<Object[]> getBookingForStatisticTicketDESC(@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticTicketDESC(start, end);
    }
    @GetMapping("/bookingForStatistic_revenue_desc")
    public  List<Object[]> getBookingForStatisticRevenueDESC(@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticRevenueDESC(start, end);
    }
    @GetMapping("/bookingForStatistic_cancel_desc")
    public  List<Object[]> getBookingForStatisticCancelDESC(@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticCancelDESC(start, end);
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
        if(this.bookingRepository.ticketOfYear(year) ==null){
            return 0;
        }
        return this.bookingRepository.ticketOfYear(year);
    }
    @GetMapping("/revenueOfYear")
    public long getRevenueOfYear(){
        Date current=new Date(System.currentTimeMillis());
        if(this.bookingRepository.revenueOfYear(current.toLocalDate().getYear())==null){
            return 0;
        }
        return this.bookingRepository.revenueOfYear(current.toLocalDate().getYear());
    }
    @GetMapping("/ticketOfToday")
    public int getTicketToday(){
        Date current=new Date(System.currentTimeMillis());
        if(this.bookingRepository.ticketOfToday(current)==null)
            return 0;
        return this.bookingRepository.ticketOfToday(current);
    }
    @GetMapping("/revenueOfToday")
    public long getRevenueOfToday(){
        Date current=new Date(System.currentTimeMillis());
        if(this.bookingRepository.revenueOfToday(current)==null)
            return 0;
        return this.bookingRepository.revenueOfToday(current);
    }
    //statistic per theater
    @GetMapping("/bookingForStatistic_ticket_asc/{id}")
    public  List<Object[]> getBookingForStatisticTicketASCTheater(@PathVariable("id") Integer id,@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticTicketASCTheater(id,start, end);
    }
    @GetMapping("/bookingForStatistic_revenue_asc/{id}")
    public  List<Object[]> getBookingForStatisticRevenueASCTheater(@PathVariable("id") Integer id,@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticRevenueASCTheater(id,start, end);
    }
    @GetMapping("/bookingForStatistic_ticket_desc/{id}")
    public  List<Object[]> getBookingForStatisticTicketDESCTheater(@PathVariable("id") Integer id,@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticTicketDESCTheater(id,start, end);
    }
    @GetMapping("/bookingForStatistic_revenue_desc/{id}")
    public  List<Object[]> getBookingForStatisticRevenueDESCTheater(@PathVariable("id") Integer id,@RequestParam("start") Date start, @RequestParam("end") Date end){
        return this.bookingRepository.bookingForStatisticRevenueDESCTheater(id,start, end);
    }
    @GetMapping("/ticketFerMonth/{id}")
    public int[] getTicketMonthlyTheater(@PathVariable("id") Integer id){
        int[] result=new int[12];
        for (int i=0;i<12;i++){
            result[i]=0;
        }
        List<Object[]> statistic= this.bookingRepository.ticketPerMonthTheater(id);
        for (Object[] objects: statistic){
            int index= (int)objects[0];
            result[index-1] =((Long) objects[1]).intValue();
        }
        return result;
    }
    @GetMapping("/revenueFerMonth/{id}")
    public int[] getRevenueMonthlyTheater(@PathVariable("id") Integer id){
        int[] result=new int[12];
        for (int i=0;i<12;i++){
            result[i]=0;
        }
        List<Object[]> statistic= this.bookingRepository.revenuePerMonthTheater(id);
        for (Object[] objects: statistic){
            int index= (int)objects[0];
            result[index-1] =((Long) objects[1]).intValue();
        }
        return result;
    }
    @GetMapping("/ticketOfYear/{id}")
    public int getTicketYearTheater(@PathVariable("id") Integer id) {
        Date current=new Date(System.currentTimeMillis());
        int year = current.toLocalDate().getYear();
        if(this.bookingRepository.ticketOfYearTheater(id,year)!=null){
            return this.bookingRepository.ticketOfYearTheater(id,year);
        }
        return 0;
    }
    @GetMapping("/revenueOfYear/{id}")
    public long getRevenueOfYearTheater(@PathVariable("id") Integer id){
        Date current=new Date(System.currentTimeMillis());
        if( this.bookingRepository.revenueOfYearTheater(id,current.toLocalDate().getYear())==null)
        {
            return 0;
        }
        return this.bookingRepository.revenueOfYearTheater(id,current.toLocalDate().getYear());
    }
    @GetMapping("/ticketOfToday/{id}")
    public int getTicketTodayTheater(@PathVariable("id") Integer id){
        Date current=new Date(System.currentTimeMillis());
        if(this.bookingRepository.ticketOfTodayTheater(id,current)==null){
            return 0;
        }
        return this.bookingRepository.ticketOfTodayTheater(id,current);
    }
    @GetMapping("/revenueOfToday/{id}")
    public long getRevenueOfTodayTheater(@PathVariable("id") Integer id) {
        Date current = new Date(System.currentTimeMillis());
        if(this.bookingRepository.revenueOfTodayTheater(id,current)==null){
            return 0;
        }
        return this.bookingRepository.revenueOfTodayTheater(id,current);
    }
    @GetMapping("/queryTotal")
    public Integer getFinalTotal(@RequestParam("total") Integer total, @RequestParam("bookingId") Integer bookingId, Principal principal){
        Optional<Booking> b = this.bookingRepository.findById(bookingId);
        Booking booking= b.get();
        User user = this.userRepository.getUserByUserName(principal.getName());
        if(user.getPoint()<=(total/1000)){
            Integer ftotal = total - (user.getPoint()*1000);
            booking.setTotal(ftotal);
            booking.setPoint(true);
            user.setPoint(0);
            this.bookingRepository.save(booking);
            this.userRepository.save(user);
            return ftotal;
        }
        else {
            booking.setTotal(0);
            booking.setPoint(true);
            user.setPoint(user.getPoint()-(total/1000));
            this.bookingRepository.save(booking);
            this.userRepository.save(user);
            return 0;
        }

    }
    @GetMapping("/queryTotalAdd")
    public Integer getTotal(@RequestParam("total") Integer total, @RequestParam("bookingId") Integer bookingId, Principal principal){
        Booking booking= this.bookingRepository.getById(bookingId);
        User user = this.userRepository.getUserByUserName(principal.getName());
        user.setPoint(user.getPoint()+((booking.getActualTotal()-booking.getTotal())/1000));
        booking.setTotal(booking.getActualTotal());
        booking.setPoint(false);
        this.userRepository.save(user);
        this.bookingRepository.save(booking);
        return booking.getActualTotal();
    }
    @GetMapping("/queryCancelOfToday/{id}")
    public Integer cancelOfToday(@PathVariable("id") Integer id){
        return this.bookingRepository.ticketCancelOfTodayTheater(id, new Date(System.currentTimeMillis()));
    }
    @GetMapping("/queryCancelOfYear/{id}")
    public Integer cancelOfYear(@PathVariable("id") Integer id){
        Date current=new Date(System.currentTimeMillis());
        int year = current.toLocalDate().getYear();
        return this.bookingRepository.ticketCancelOfYearTheater(id, year);
    }
    @GetMapping("/ticketCancelOfToday")
    public Integer cancelOfTodayALl(){
        return this.bookingRepository.ticketCancelOfToday( new Date(System.currentTimeMillis()));
    }
    public Double round(Double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
    @GetMapping("/ratioCancelOfToday")
    public Double cancelOfYearAll(){
        int cancel = this.bookingRepository.ticketCancelOfToday( new Date(System.currentTimeMillis()));
        int ticket = this.bookingRepository.ticketOfToday(new Date(System.currentTimeMillis()));
        Double ratio= (double) cancel / ticket * 100;
        return round(ratio, 2);
    }
}
