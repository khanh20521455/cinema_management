package cinema_management.entities;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="MOVIE")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String time;
    private String genre;
    private String director;
    private  String actor;
    private int year;
    private  Date start;
    private Date end;
    private String poster;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<Comment> commentList;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "movie")
    private List<Showtimes> showtimesList;
}
