package cinema_management.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="COMMENT")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name="movieId")
    private Movie movie;
    private String comment;



}
