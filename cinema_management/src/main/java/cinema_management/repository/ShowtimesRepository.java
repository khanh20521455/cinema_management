package cinema_management.repository;

import cinema_management.entities.Showtimes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;

@Repository
public interface ShowtimesRepository extends JpaRepository<Showtimes, Integer> {
}
