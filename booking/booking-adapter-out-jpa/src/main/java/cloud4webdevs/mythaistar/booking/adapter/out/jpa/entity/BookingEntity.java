package cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity;

import cloud4webdevs.mythaistar.booking.domain.BookingStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "booking")
public class BookingEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Instant creationDate;

    private Instant bookingFromTime;

    private Instant bookingToTime;

    private Instant bookingDate;

    private Instant expirationDate;

    private String email;

    private int seatsNumber;

    private BookingStatus status;

    private String token;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "booking_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<TableBookingEntity> tableBookings = new HashSet<>();
}
