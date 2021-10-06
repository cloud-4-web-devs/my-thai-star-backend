package cloud4webdevs.mythaistar.booking.adapter.out.jpa.mapper;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity.BookingEntity;
import cloud4webdevs.mythaistar.booking.domain.Booking;
import cloud4webdevs.mythaistar.booking.domain.TableBooking;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TableBookingMapper.class})
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target="id.value", source="id")
    Booking toDomain(BookingEntity bookingEntity);

    @Mapping(target="id", source="id.value")
    @Mapping(target="tableBookings", ignore = true)
    void toEntity(Booking booking, @MappingTarget BookingEntity bookingEntity);
}
