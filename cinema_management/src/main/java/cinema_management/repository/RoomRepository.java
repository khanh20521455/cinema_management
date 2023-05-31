package cinema_management.repository;

import cinema_management.entities.Purchase;
import cinema_management.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("FROM Room as r ORDER  BY r.name")
    public Page<Room> findAllOrderByRoomNameDateAsc(Pageable pageable);
}
