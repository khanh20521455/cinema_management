package cinema_management.repository;

import cinema_management.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("FROM Room as r ORDER  BY r.name")
    public Page<Room> findAllOrderByRoomNameAsc(Pageable pageable);

    @Query("FROM Room as r ORDER  BY r.name")
    public List<Room> roomList();

    @Query("FROM Room as r WHERE r.theater.id=:id AND r.status='Đang hoạt động'")
    public List<Room> roomActive(@PathVariable("id") Integer id);

    @Query("From Room as r Where r.theater.id=:id Order By r.name ASC")
    public Page<Room> roomBaseTheater(@PathVariable("id") Integer id, Pageable pageable);

}
