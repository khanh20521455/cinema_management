package cinema_management.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    private String status;

    public Room() {
    }
    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private List<Showtimes> showtimesList;

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

    public String getTypeScreen() {
        return typeScreen;
    }

    public void setTypeScreen(String typeScreen) {
        this.typeScreen = typeScreen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Showtimes> getShowtimesList() {
        return showtimesList;
    }

    public void setShowtimesList(List<Showtimes> showtimesList) {
        this.showtimesList = showtimesList;
    }
}
