package cinema_management.repository;

import cinema_management.entities.Movie;
import cinema_management.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Date;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query("FROM Movie as m ORDER  BY m.name")
    public Page<Movie> findAllOrderByMovieDateAsc(Pageable pageable);
    @Query("FROM Movie as m ORDER  BY m.name")
    public List<Movie> movieList();
    @Query("FROM Movie as m WHERE m.start > :now")
    public List<Movie> movieUpcoming(@Param("now") Date now);
    @Query("FROM Movie as m WHERE m.start <= :now and m.end >= :now")
    public List<Movie> moviePlaying(@Param("now") Date now);
    public List<Movie> findByNameContainingAndEndAfter (String name, @Param("now")Date now);
    public List<Movie> findByNameContaining(String name);
    @Query("FROM Movie as m where m.end >= :now")
    public List<Movie> movieUpcomingAndPlaying(@Param("now")Date now);

}
