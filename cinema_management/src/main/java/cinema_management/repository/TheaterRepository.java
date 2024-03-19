package cinema_management.repository;

import cinema_management.entities.Showtimes;
import cinema_management.entities.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterRepository extends JpaRepository<Theater,Integer> {
    @Query("FROM Theater as t ORDER BY t.name Asc ")
    public Page<Theater> findAllOrderByNameAsc(Pageable pageable);
    @Query ("SELECT DISTINCT t.province FROM Theater as t ORDER BY t.name Asc")
    public List<String> listProvince();
}
