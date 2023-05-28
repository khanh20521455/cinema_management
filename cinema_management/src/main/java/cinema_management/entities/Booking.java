package cinema_management.entities;

import javax.persistence.*;
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
    private int status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "booking")
    private List<Seat> seatList;

}
