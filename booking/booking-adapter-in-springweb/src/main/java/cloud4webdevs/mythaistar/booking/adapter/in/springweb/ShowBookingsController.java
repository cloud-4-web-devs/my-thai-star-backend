package cloud4webdevs.mythaistar.booking.adapter.in.springweb;

import cloud4webdevs.mythaistar.booking.port.in.ShowBookingsQuery;
import cloud4webdevs.mythaistar.booking.port.in.ShowBookingsResult;
import cloud4webdevs.mythaistar.booking.port.in.ShowBookingsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("booking")
public class ShowBookingsController {

    private final ShowBookingsUseCase showBookingsUseCase;

    @GetMapping(value = "/bookings")
    public List<ShowBookingsResult> showBookings() {
        // in the future, ShowBookingsQuery may contain something interesting
        return showBookingsUseCase.showBookings(new ShowBookingsQuery());
    }
}
