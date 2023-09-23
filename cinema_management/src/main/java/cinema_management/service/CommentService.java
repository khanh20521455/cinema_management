package cinema_management.service;

import cinema_management.entities.Booking;
import cinema_management.entities.Comment;
import cinema_management.entities.Movie;
import cinema_management.repository.BookingRepository;
import cinema_management.repository.CommentRepository;
import cinema_management.repository.MovieRepository;
import cinema_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
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
        return "normaluser/comment/comment";
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
            comment.setStatus(0);
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
        return "normaluser/comment/update_comment";
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
            comment.setStatus(0);
            commentRepository.save(comment);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/user/movie_watched/0";
    }
    public String getCommentWaitConfirm(Integer page, Model model) {

        Pageable pageable = PageRequest.of(page, 10);
        Page<Comment> commentList= commentRepository.commentWaitConfirm(pageable);
        model.addAttribute("commentList", commentList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", commentList.getTotalPages());
        return "adminuser/comment_management";
    }
    public String confirmComment(Integer id){
        Comment comment= this.commentRepository.getById(id);
        comment.setStatus(1);
        commentRepository.save(comment);
        return "redirect:/admin/comment_management/0";
    }
    public String cancelComment(Integer id){
        Comment comment= this.commentRepository.getById(id);
        comment.setStatus(2);
        commentRepository.save(comment);
        return "redirect:/admin/comment_management/0";
    }
    public String getCommentList(Integer page, Model model){
        Pageable pageable = PageRequest.of(page, 10);
        Page<Comment> commentList= this.commentRepository.commentWaitConfirm(pageable);
        model.addAttribute("commentList", commentList);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",commentList.getTotalPages());
        return "adminuser/confirm_comment";
    }
}
