package cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity.TableBookingEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TableBookingRepository extends CrudRepository<TableBookingEntity, Long> {

    // TODO change compiler settings to use -parameters switch
    @Query("from TableBookingEntity where (bookingFrom <= :fromTime and bookingTo >= :fromTime) or (bookingFrom <= :toTime and bookingTo >= :toTime) or (bookingFrom > :fromTime and bookingTo < :toTime)")
    List<TableBookingEntity> findBookingsIntersect(@Param("fromTime") Instant from, @Param("toTime") Instant to);
}
