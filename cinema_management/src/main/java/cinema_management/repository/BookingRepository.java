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

    @Query("FROM Booking as b WHERE b.status=1 ORDER BY b.showtimes.date DESC, b.showtimes.time ASC")
    public Page<Booking> bookingWaitConfirm(Pageable pageable);

    @Query("SELECT MIN(b) FROM Booking b WHERE b.status = 2 AND b.showtimes.date <= :now AND b.user.email = :username GROUP BY b.showtimes.movie.id")
    public Page<Booking> findFirstByStatusAndShowtimesDateLessThanEqualAndUserEmail(@Param("now")Date now, @Param("username") String email, Pageable pageable);

    @Query("FROM Booking as b WHERE b.user.email=:username ORDER BY b.showtimes.date DESC")
    public Page<Booking> bookingHistory(@Param("username") String email, Pageable pageable);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total) FROM Booking as b WHERE b.status = 2 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.numberOfSeat) DESC")
    public List<Object[]> bookingForStatisticTicketDESC(@RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total) FROM Booking as b WHERE b.status = 2 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.total) DESC")
    public List<Object[]> bookingForStatisticRevenueDESC(@RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total) FROM Booking as b WHERE b.status = 2 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.numberOfSeat) ASC")
    public List<Object[]> bookingForStatisticTicketASC(@RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT b.showtimes.movie.id, b.showtimes.movie.poster ,b.showtimes.movie.name, SUM(b.numberOfSeat), SUM(b.total) FROM Booking as b WHERE b.status = 2 AND b.completedAt >= :start AND" +
            " b.completedAt <= :end GROUP BY b.showtimes.movie.id ORDER BY SUM(b.total) ASC")
    public List<Object[]> bookingForStatisticRevenueASC(@RequestParam("start") Date start, @RequestParam("end") Date end);

    @Query("SELECT MONTH(b.completedAt),SUM(b.numberOfSeat) FROM Booking as b WHERE b.status = 2 GROUP BY MONTH(b.completedAt)")
    public List<Object[]> ticketPerMonth();

    @Query("SELECT MONTH(b.completedAt),SUM(b.total) FROM Booking as b WHERE b.status = 2 GROUP BY MONTH(b.completedAt)")
    public List<Object[]> revenuePerMonth();

    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE b.status=2 AND YEAR(b.completedAt)=:year")
    public int ticketOfYear(@Param("year") int year);

    @Query("SELECT SUM(b.total) FROM Booking as b WHERE b.status=2 AND YEAR(b.completedAt)=:year")
    public long revenueOfYear(@Param("year") int year);

    @Query("SELECT SUM(b.numberOfSeat) FROM Booking as b WHERE b.status=2 AND b.completedAt=:now")
    public int ticketOfToday(@Param("now") Date now);

    @Query("SELECT SUM(b.total) FROM Booking as b WHERE b.status=2 AND b.completedAt=:now")
    public long revenueOfToday(@Param("now") Date now);

}
