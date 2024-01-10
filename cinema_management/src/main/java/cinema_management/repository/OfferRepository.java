package cinema_management.repository;

import cinema_management.entities.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
    @Query("FROM Offer as o ORDER  BY o.startAt DESC")
    public Page<Offer> getAll(Pageable pageable);
}
