package cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Data
@Table(name="table_booking")
public class TableBookingEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "booking_from")
    private Instant bookingFrom;

    @Column(name = "booking_to")
    private Instant bookingTo;

    @Column(name = "seats_number")
    private int seatsNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private TableEntity table;
}
