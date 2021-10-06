package cloud4webdevs.mythaistar.booking.service;

import cloud4webdevs.mythaistar.booking.domain.TableBooking;
import cloud4webdevs.mythaistar.booking.domain.TableId;
import cloud4webdevs.mythaistar.booking.port.in.ShowBookingsQuery;
import cloud4webdevs.mythaistar.booking.port.in.ShowBookingsResult;
import cloud4webdevs.mythaistar.booking.port.in.ShowBookingsUseCase;
import cloud4webdevs.mythaistar.booking.port.out.FindBookingsPort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShowBookingsService implements ShowBookingsUseCase {

    private final FindBookingsPort findBookingsPort;

    @Override
    public List<ShowBookingsResult> showBookings(ShowBookingsQuery showBookingsQuery) {
        return findBookingsPort.findBookings().stream()
                .map(booking -> new ShowBookingsResult(
                        booking.getEmail(),
                        booking.getBookingFromTime(),
                        booking.getBookingToTime(),
                        booking.getTableBookings().stream()
                                .findFirst()
                                .map(TableBooking::getTableId)
                                .map(TableId::getValue)
                                .orElse(null),
                        booking.getSeatsNumber(),
                        booking.getToken(),
                        booking.getStatus().name()))
                .collect(Collectors.toList());
    }
}
