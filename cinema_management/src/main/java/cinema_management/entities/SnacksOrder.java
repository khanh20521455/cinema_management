package cinema_management.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name="SNACKSORDER")
public class SnacksOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "snacksId")
    private Snacks snacks;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bookingId")
    private Booking booking;
    private int quantity;

    public SnacksOrder(int id, Snacks snacks, Booking booking, int quantity) {
        this.id = id;
        this.snacks = snacks;
        this.booking = booking;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Snacks getSnacks() {
        return snacks;
    }

    public void setSnacks(Snacks snacks) {
        this.snacks = snacks;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
