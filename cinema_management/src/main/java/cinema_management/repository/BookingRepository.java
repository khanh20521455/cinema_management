package cinema_management.repository;

import cinema_management.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import cinema_management.entities.Movie;
import java.sql.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("FROM Booking as b WHERE b.showtimes.id=:id")
    public List<Booking> bookingBaseShowtimes(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Booking as s WHERE s.showtimes.id=:id")
    public void deleteBookingBaseShowtimes(@PathVariable("id") Integer id);

    @Query("FROM Booking as b WHERE b.status=1  AND b.showtimes.room.theater.id=:id ORDER BY b.showtimes.date DESC, b.showtimes.time ASC")
    public Page<Booking> bookingWaitConfirm(@PathVariable("id") Integer id,  Pageable pageable);

    @Query("SELECT MIN(b) FROM Booking b WHERE b.status = 1 AND b.showtimes.date <= :now AND b.user.email = :username GROUP BY b.showtimes.movie.id")
    public Page<Booking> findFirstByStatusAndShowtimesDateLessThanEqualAndUserEmail(@Param("now")Date now, @Param("username") String email, Pageable pageable);

    @Query("FROM Booking as b WHERE b.user.email=:username ORDER BY b.showtimes.date DESC")
    public Page<Booking> bookingHistory(@Param("username") String email, Pageable pageable);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, b.showtimes.movie.tickets, SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.numberOfSeat) DESC")
    public List<Object[]> bookingForStatisticTicketDESC(@RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name,  b.showtimes.movie.tickets, SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.total) DESC")
    public List<Object[]> bookingForStatisticRevenueDESC(@RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name,  b.showtimes.movie.tickets, SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.numberOfSeat) ASC")
    public List<Object[]> bookingForStatisticTicketASC(@RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name,  b.showtimes.movie.tickets, SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.total) ASC")
    public List<Object[]> bookingForStatisticRevenueASC(@RequestParam("start") Date start, @RequestParam("end") Date end);
    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name,  b.showtimes.movie.tickets, SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY b.showtimes.movie.cancel DESC")
    public List<Object[]> bookingForStatisticCancelDESC(@RequestParam("start") Date start, @RequestParam("end") Date end);
    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name,  b.showtimes.movie.tickets, SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY b.showtimes.movie.cancel ASC")
    public List<Object[]> bookingForStatisticCancelASC(@RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT MONTH(b.completedAt),SUM(b.numberOfSeat) FROM Booking as b WHERE b.status = 1 GROUP BY MONTH(b.completedAt)")
    public List<Object[]> ticketPerMonth();

    @Query("SELECT MONTH(b.completedAt),SUM(b.total) FROM Booking as b WHERE b.status = 1 GROUP BY MONTH(b.completedAt)")
    public List<Object[]> revenuePerMonth();

    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE  YEAR(b.completedAt)=:year")
    public Integer ticketOfYear(@Param("year") int year);

    @Query("SELECT SUM(b.total) FROM Booking as b WHERE  YEAR(b.completedAt)=:year")
    public Long revenueOfYear(@Param("year") int year);

    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE b.completedAt=:now")
    public Integer ticketOfToday(@Param("now") Date now);

    @Query("SELECT SUM(b.total) FROM Booking as b WHERE b.status=1 AND b.completedAt=:now")
    public Long revenueOfToday(@Param("now") Date now);

    // Statistic for per theater
    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.showtimes.room.theater.id=:id AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.numberOfSeat) DESC")
    public List<Object[]> bookingForStatisticTicketDESCTheater(@PathVariable("id") Integer id, @RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.showtimes.room.theater.id=:id AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.total) DESC")
    public List<Object[]> bookingForStatisticRevenueDESCTheater(@PathVariable("id") Integer id, @RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.showtimes.room.theater.id=:id AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.numberOfSeat) ASC")
    public List<Object[]> bookingForStatisticTicketASCTheater(@PathVariable("id") Integer id, @RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.showtimes.room.theater.id=:id AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.total) ASC")
    public List<Object[]> bookingForStatisticRevenueASCTheater(@PathVariable("id") Integer id, @RequestParam("start") Date start, @RequestParam("end") Date end);
    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.showtimes.room.theater.id=:id AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM( b.showtimes.movie.cancel) ASC")
    public List<Object[]> bookingForStatisticCancelASCTheater(@PathVariable("id") Integer id, @RequestParam("start") Date start, @RequestParam("end") Date end);
    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total), b.showtimes.movie.cancel FROM Booking as b WHERE b.status = 1 AND b.showtimes.room.theater.id=:id AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.showtimes.movie.cancel) DESC")
    public List<Object[]> bookingForStatisticCancelDESCTheater(@PathVariable("id") Integer id, @RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT MONTH(b.completedAt),SUM(b.numberOfSeat) FROM Booking as b WHERE b.showtimes.room.theater.id=:id AND b.status = 1 GROUP BY MONTH(b.completedAt)")
    public List<Object[]> ticketPerMonthTheater(@PathVariable("id") Integer id);

    @Query("SELECT MONTH(b.completedAt),SUM(b.total) FROM Booking as b WHERE b.showtimes.room.theater.id=:id AND b.status = 1 GROUP BY MONTH(b.completedAt)")
    public List<Object[]> revenuePerMonthTheater(@PathVariable("id") Integer id);

    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE b.showtimes.room.theater.id=:id AND  YEAR(b.completedAt)=:year")
    public Integer ticketOfYearTheater(@PathVariable("id") Integer id, @Param("year") int year);

    @Query("SELECT SUM(b.total) FROM Booking as b WHERE b.showtimes.room.theater.id=:id AND b.status=1 AND YEAR(b.completedAt)=:year")
    public Long revenueOfYearTheater(@PathVariable("id") Integer id, @Param("year") int year);

    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE b.showtimes.room.theater.id=:id AND b.completedAt=:now")
    public Integer ticketOfTodayTheater(@PathVariable("id") Integer id, @Param("now") Date now);

    @Query("SELECT SUM(b.total) FROM Booking as b WHERE b.showtimes.room.theater.id=:id AND b.status=1 AND b.completedAt=:now")
    public Long revenueOfTodayTheater(@PathVariable("id") Integer id, @Param("now") Date now);

    @Query("SELECT b.showtimes.movie FROM Booking as b WHERE b.showtimes.movie.end >= :now GROUP BY b.showtimes.movie ORDER BY SUM(b.numberOfSeat) DESC ")
    public List<Movie> listMovieBestSale(@Param("now") Date now, Pageable pageable);
    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE b.showtimes.room.theater.id=:id AND b.status=2 AND b.cancelDate=:now")
    public Integer ticketCancelOfTodayTheater(@PathVariable("id") Integer id, @Param("now") Date now);
    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE b.showtimes.room.theater.id=:id AND b.status=2 AND YEAR(b.cancelDate)=:year")
    public Integer ticketCancelOfYearTheater(@PathVariable("id") Integer id, @Param("year") int year);
    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE  b.status=2 AND b.cancelDate=:now")
    public Integer ticketCancelOfToday( @Param("now") Date now);
    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE  b.status=2 AND YEAR(b.cancelDate)=:year")
    public Integer ticketCancelOfYear( @Param("year") int year);
}

