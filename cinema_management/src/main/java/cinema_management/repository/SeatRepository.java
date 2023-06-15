package cinema_management.repository;

import cinema_management.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Seat as s WHERE s.showtimes.id=:id")
    public void deleteSeatBaseShowtimes(@PathVariable("id") Integer id);
}
