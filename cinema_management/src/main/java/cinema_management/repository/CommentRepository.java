package cinema_management.repository;

import cinema_management.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("FROM Comment as c WHERE c.user.email=:email AND c.movie.id=:movieId")
    public Comment commentExist(@Param("email") String email, @Param("movieId") Integer id);

    @Query("FROM Comment as c WHERE c.movie.id=:movieId")
    public List<Comment> commentMovie(@Param("movieId") Integer id);
}
