package cinema_management.repository;

import cinema_management.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("FROM Comment as c WHERE c.user.email=:email AND c.movie.id=:movieId")
    public Comment commentExist(@Param("email") String email, @Param("movieId") Integer id);

    @Query("FROM Comment as c WHERE c.movie.id=:movieId")
    public List<Comment> commentMovie(@Param("movieId") Integer id);
    @Transactional
    @Modifying
    @Query("DELETE FROM Comment as s WHERE s.movie.id=:id")
    public void deleteCommontMovie(@Param("id") Integer id);
}
