package cinema_management.repository;

import cinema_management.entities.Movie;
import cinema_management.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
    @Query("FROM Movie as m ORDER  BY m.name")
    public Page<Movie> findAllOrderByMovieDateAsc(Pageable pageable);

}
