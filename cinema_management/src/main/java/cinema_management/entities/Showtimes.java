package cinema_management.entities;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="SHOWTIMES")
public class Showtimes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name="movieId")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name="roomId")
    private Room room;
    private LocalDateTime time;
    private int price;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "showtimes")
    private List<Booking> bookingList;

}
