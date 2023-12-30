package cinema_management.repository;

import cinema_management.entities.Room;
import cinema_management.entities.Showtimes;
import cinema_management.entities.Theater;
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

import javax.persistence.Entity;
import java.sql.Date;
import java.util.List;

@Repository
public interface ShowtimesRepository extends JpaRepository<Showtimes, Integer> {
    @Query("FROM Showtimes as s  WHERE s.room.theater.id=:id AND s.status = 1 ORDER  BY s.date DESC, s.time DESC")
    public Page<Showtimes> findAllOrderByDateDesc(@PathVariable("id") Integer id,Pageable pageable);

    @Query("FROM Showtimes as s WHERE s.movie.id=:id")
    public List<Showtimes> findByMovieId(@PathVariable("id") Integer id);

    @Query("FROM Showtimes as s WHERE s.room.id=:id")
    public List<Showtimes> findByRoomId(@PathVariable("id") Integer id);

    @Query("FROM Showtimes as s WHERE s.movie.id=:id")
    public List<Showtimes> ShowtimesBaseOnMovie(@PathVariable("id") Integer id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Showtimes as s WHERE s.room.id=:id")
    public  void deleteBaseOnRoom(@PathVariable("id") Integer id);

    @Query("SELECT DISTINCT s.room.typeScreen FROM Showtimes as s WHERE s.movie.id=:id and s.date=:date and s.room.theater.id =:theaterId")
    public List<String> typeScreenShowtimes(@RequestParam("id") Integer id, @RequestParam("theaterId") Integer theaterId, @RequestParam("date") Date date);

    @Query("FROM Showtimes as s WHERE s.movie.id=:id and s.room.theater.id =:theaterId and s.date=:date and s.room.typeScreen=:typeScreen")
    public List<Showtimes> timeShowtimes(@RequestParam("id") Integer id, @RequestParam("theaterId") Integer theaterId, @RequestParam("date") Date date, @RequestParam("typeScreen")String typeScreen);
    @Query("SELECT MAX(id) FROM Showtimes")
    public Integer newShowtime();

    @Query("SELECT DISTINCT s.room.theater FROM Showtimes as s WHERE s.movie.id = :id and s.room.theater.province =:province")
    public List<Theater> theaterShowtimes(@RequestParam("id") Integer id, @RequestParam("province") String province);

}
