package cinema_management.service;

import cinema_management.entities.Comment;
import cinema_management.entities.Movie;
import cinema_management.repository.BookingRepository;
import cinema_management.repository.CommentRepository;
import cinema_management.repository.MovieRepository;
import cinema_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    public String addComment(Integer id, Principal principal, Model model){
        Movie movie= movieRepository.getById(id);
        Comment comment=new Comment();
        comment.setMovie(movie);
        comment.setUser(this.userRepository.getUserByUserName(principal.getName()));
        commentRepository.save(comment);
        model.addAttribute("comment", comment);
        return "normaluser/comment";
    }
    public String addCommentProcess(Integer id, Comment comment, int rating){
        Optional<Comment> optional= this.commentRepository.findById(id);
        Comment oldComment= optional.get();
        try {
            comment.setId(oldComment.getId());
            comment.setMovie(oldComment.getMovie());
            comment.setUser(oldComment.getUser());
            comment.setRating(rating);
            comment.setCommentString(comment.getCommentString());
            commentRepository.save(comment);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/user/movie_watched/0";
    }

    public String updateComment(Integer id, Principal principal, Model model){
        Movie movie= movieRepository.getById(id);
        Comment comment= this.commentRepository.commentExist(principal.getName(),id);
        model.addAttribute("comment", comment);
        return "normaluser/update_comment";
    }
    public String updateCommentProcess(Integer id, Comment comment, int rating){
        Optional<Comment> optional= this.commentRepository.findById(id);
        Comment oldComment= optional.get();
        try {
            comment.setId(oldComment.getId());
            comment.setMovie(oldComment.getMovie());
            comment.setUser(oldComment.getUser());
            comment.setRating(rating);
            comment.setCommentString(comment.getCommentString());
            commentRepository.save(comment);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/user/movie_watched/0";
    }
}
