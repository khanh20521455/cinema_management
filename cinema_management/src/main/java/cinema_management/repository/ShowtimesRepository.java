package cinema_management.repository;

import cinema_management.entities.Showtimes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShowtimesRepository extends JpaRepository<Showtimes, Integer> {
}
