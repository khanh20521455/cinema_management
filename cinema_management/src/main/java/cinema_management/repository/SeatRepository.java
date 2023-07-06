package cinema_management.repository;

import cinema_management.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Seat as s WHERE s.showtimes.id=:id")
    public void deleteSeatBaseShowtimes(@PathVariable("id") Integer id);
    @Query("FROM Seat as s WHERE s.showtimes.id=:id")
    public List<Seat> seatBaseShowtimes(@Param("id") Integer id);
    @Query("FROM Seat as s WHERE s.showtimes.id=:id AND s.status=1")
    public List<Seat> seatNotSelectYet(@RequestParam("id") Integer id);
    @Query("FROM Seat as s WHERE s.showtimes.id=:id AND s.seat=:seat")
    public Seat choosingSeat(@Param("id") Integer id,@Param("seat") Integer seat);
    @Query("FROM Seat as s WHERE s.id=:id")
    public Seat getSeat(@RequestParam("id") Integer id);
}
