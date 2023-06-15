package cinema_management.entities;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Entity
@Table(name="BOOKING")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    @JoinColumn(name="showtimesId")
    private Showtimes showtimes;
    private int numberOfSeat;
    private Integer[] seatList;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Showtimes getShowtimes() {
        return showtimes;
    }

    public void setShowtimes(Showtimes showtimes) {
        this.showtimes = showtimes;
    }

    public int getNumberOfSeat() {
        return numberOfSeat;
    }

    public void setNumberOfSeat(int numberOfSeat) {
        this.numberOfSeat = numberOfSeat;
    }

    public Integer[] getSeatList() {
        return seatList;
    }

    public void setSeatList(Integer[] seatList) {
        this.seatList = seatList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Booking() {
    }
}
