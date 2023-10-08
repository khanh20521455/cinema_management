package cinema_management.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.sql.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="MOVIE")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    //Thời lượng phim
    private String time;
    private String[] genre;
    private String director;
    private  String actor;
    private int year;
    private  Date start;
    private Date end;
    private String poster;
    private int tickets;
    private long revenue;

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }
    @JsonBackReference
    @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY, mappedBy = "movie")
    private List<Comment> commentList;
    @JsonBackReference
    @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.LAZY,mappedBy = "movie")
    private List<Showtimes> showtimesList;

    public Movie() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<Showtimes> getShowtimesList() {
        return showtimesList;
    }

    public void setShowtimesList(List<Showtimes> showtimesList) {
        this.showtimesList = showtimesList;
    }
    @Override
    public String toString() {
        return "Movie [id=" + id + ", name=" + name +",time="+time+ ", genre=" + genre + ", director =" + director
                + ", actor=" + actor + ", year=" + year + ", start=" + start  + ", end=" + end
                + ", poster=" + poster
                + "]";
    }
}
