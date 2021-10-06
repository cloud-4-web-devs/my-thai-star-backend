package cloud4webdevs.mythaistar.springboot.adapter.out;

import cloud4webdevs.mythaistar.booking.port.out.BookingConfimationEvent;
import cloud4webdevs.mythaistar.booking.port.out.SendBookingConfirmationPort;
import org.springframework.stereotype.Service;

@Service
public class SendBookingConfirmationDummyAdapter implements SendBookingConfirmationPort {
    @Override
    public void send(BookingConfimationEvent event) {
        // just to fulfil dependency, normally we would like to dispatch an event to notification component
    }
}
