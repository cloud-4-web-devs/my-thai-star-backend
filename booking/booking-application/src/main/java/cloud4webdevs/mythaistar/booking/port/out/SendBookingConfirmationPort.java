package cloud4webdevs.mythaistar.booking.port.out;

public interface SendBookingConfirmationPort {
    void send(BookingConfimationEvent event);
}
