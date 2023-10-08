package cinema_management.repository;

import cinema_management.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("FROM Room as r ORDER  BY r.name")
    public Page<Room> findAllOrderByRoomNameAsc(Pageable pageable);

    @Query("FROM Room as r ORDER  BY r.name")
    public List<Room> roomList();

    @Query("FROM Room as r WHERE r.status='Đang hoạt động'")
    public List<Room> roomActive();
}
