package cinema_management.repository;

import cinema_management.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("FROM Booking as b WHERE b.showtimes.id=:id")
    public List<Booking> bookingBaseShowtimes(@Param("id") Integer id);
    @Transactional
    @Modifying
    @Query("DELETE FROM Booking as s WHERE s.showtimes.id=:id")
    public void deleteBookingBaseShowtimes(@PathVariable("id") Integer id);
}
