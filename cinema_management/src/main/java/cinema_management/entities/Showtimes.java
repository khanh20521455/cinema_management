package cinema_management.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="SHOWTIMES")
public class Showtimes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="movieId")
    private Movie movie;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="roomId")
    private Room room;
    private String time;
    private Date date;
    private int price;
    @JsonBackReference
    @OneToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER, mappedBy = "showtimes")
    private List<Booking> bookingList;

    public Showtimes() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }



}
