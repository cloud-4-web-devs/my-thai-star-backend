package cloud4webdevs.mythaistar.booking.adapter.out.jpa;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity.BookingEntity;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.mapper.BookingMapper;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.BookingRepository;
import cloud4webdevs.mythaistar.booking.domain.Booking;
import cloud4webdevs.mythaistar.booking.port.out.PersistBookingPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PersistBookingJpaAdapter implements PersistBookingPort {

    private final BookingRepository bookingRepository;

    private final BookingMapper mapper = BookingMapper.INSTANCE;

    public Booking persist(Booking booking) {
        final BookingEntity entity = new BookingEntity();
        mapper.toEntity(booking, entity);
        return mapper.toDomain(bookingRepository.save(entity));
    }
}
