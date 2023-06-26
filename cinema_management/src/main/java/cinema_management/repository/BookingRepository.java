package cinema_management.repository;

import cinema_management.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("FROM Booking as b WHERE b.status=2 AND b.showtimes.date <=:now AND b.user.email=:username")
    public Page<Booking> bookingComment(@Param("now")Date now, @Param("username") String email, Pageable pageable);
    @Query("FROM Booking as b WHERE b.user.email=:username ORDER BY b.showtimes.date DESC")
    public Page<Booking> bookingHistory(@Param("username") String email, Pageable pageable);
}
