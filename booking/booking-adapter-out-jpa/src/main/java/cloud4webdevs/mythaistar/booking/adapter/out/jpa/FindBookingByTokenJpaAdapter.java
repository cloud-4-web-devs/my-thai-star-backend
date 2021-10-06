package cloud4webdevs.mythaistar.booking.adapter.out.jpa;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.mapper.BookingMapper;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.BookingRepository;
import cloud4webdevs.mythaistar.booking.domain.Booking;
import cloud4webdevs.mythaistar.booking.port.out.FindBookingByTokenPort;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class FindBookingByTokenJpaAdapter implements FindBookingByTokenPort {

    private final BookingRepository bookingRepository;

    private final BookingMapper mapper = BookingMapper.INSTANCE;

    @Override
    public Optional<Booking> find(String token) {
        return bookingRepository.findBookingEntityByToken(token).map(mapper::toDomain);
    }
}
