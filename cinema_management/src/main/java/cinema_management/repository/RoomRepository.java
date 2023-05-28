package cinema_management.repository;

import cinema_management.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
