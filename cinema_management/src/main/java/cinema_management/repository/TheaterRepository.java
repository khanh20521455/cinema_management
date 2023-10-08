package cinema_management.repository;

import cinema_management.entities.Showtimes;
import cinema_management.entities.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater,Integer> {
    @Query("FROM Theater as t ORDER BY t.name Desc ")
    public Page<Theater> findAllOrderByNameDesc(Pageable pageable);
}
