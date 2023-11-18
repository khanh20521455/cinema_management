package cinema_management.repository;

import cinema_management.entities.Snacks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface SnacksRepository extends JpaRepository<Snacks,Integer> {
    @Query("FROM Snacks as s ORDER  BY s.name")
    public Page<Snacks> getAll(Pageable pageable);
}
