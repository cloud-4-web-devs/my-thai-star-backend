package cloud4webdevs.mythaistar.booking.adapter.out.jpa;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity.BookingEntity;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity.TableBookingEntity;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.mapper.BookingMapper;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.BookingRepository;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.TableBookingRepository;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.TableRepository;
import cloud4webdevs.mythaistar.booking.domain.Booking;
import cloud4webdevs.mythaistar.booking.domain.exception.BookingNotFoundException;
import cloud4webdevs.mythaistar.booking.port.out.SaveBookingPort;
import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SaveBookingJpaAdapter implements SaveBookingPort {

    private final BookingRepository bookingRepository;

    private final TableBookingRepository tableBookingRepository;

    private final TableRepository tableRepository;

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;

    @Override
    public void save(Booking booking) {

        final BookingEntity bookingEntity = new BookingEntity();
        bookingMapper.toEntity(booking, bookingEntity);

        final Map<Long, Long> tableToTableBookingMap = bookingRepository.findById(booking.getId().getValue())
                .map(BookingEntity::getTableBookings)
                .map(tableBookingEntities -> tableBookingEntities.stream()
                        .collect(Collectors.toMap(
                                tableBookingEntity -> tableBookingEntity.getTable().getId(),
                                TableBookingEntity::getId
                        ))).orElseThrow(() -> new BookingNotFoundException(booking.getId()));

        bookingEntity.setTableBookings(booking.getTableBookings().stream().map(tableBooking -> {
            final TableBookingEntity entity = new TableBookingEntity();
            mapFromBooking(booking, entity);
            entity.setSeatsNumber(tableBooking.getSeatsNumber());
//            entity.setBooking(bookingEntity);
            entity.setTable(tableRepository.getById(tableBooking.getTableId().getValue()));
            Optional.ofNullable(tableToTableBookingMap.get(tableBooking.getTableId().getValue())).ifPresent(entity::setId);
            return entity;
        }).collect(Collectors.toSet()));

        bookingRepository.save(bookingEntity);
    }

    private void mapFromBooking(Booking booking, TableBookingEntity entity) {
        entity.setBookingFrom(booking.getBookingFromTime());
        entity.setBookingTo(booking.getBookingToTime());
    }
}
