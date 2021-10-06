package cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "table_entity")
public class TableEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "max_seats")
    private int maxSeats;
}
