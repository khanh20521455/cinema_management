package cinema_management.entities;

import javax.persistence.*;

@Entity
@Table(name="SEAT")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;
    private int seat;
    private int status;

}
