package cloud4webdevs.mythaistar.booking.adapter.out.jpa.mapper;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity.TableBookingEntity;
import cloud4webdevs.mythaistar.booking.domain.TableBooking;
import cloud4webdevs.mythaistar.booking.domain.TableId;

public class TableBookingMapper {

    public static final TableBookingMapper INSTANCE = new TableBookingMapper();

    public TableBooking toDomain(TableBookingEntity tableBookingEntity) {
        return TableBooking.builder()
                .tableId(new TableId(tableBookingEntity.getTable().getId()))
                .seatsNumber(tableBookingEntity.getSeatsNumber())
                .build();
    }
}
