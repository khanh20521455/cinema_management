package cinema_management.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="ROOM")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String typeScreen;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private List<Showtimes> showtimesList;
}
