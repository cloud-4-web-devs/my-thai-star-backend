package cloud4webdevs.mythaistar.springboot.configuration;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.*;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.BookingRepository;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.TableBookingRepository;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.TableRepository;
import cloud4webdevs.mythaistar.booking.port.out.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Adapter configuration for Booking component and JPA framework.
 */
@AllArgsConstructor
@Configuration
public class BookingAdapterOutJpaConfiguration {

    private final TableBookingRepository tableBookingRepository;

    private final BookingRepository bookingRepository;

    private final TableRepository tableRepository;

    @Bean
    public FindBookingByTokenPort findBookingByTokenPort() {
        return new FindBookingByTokenJpaAdapter(bookingRepository);
    }

    @Bean
    public FindFreeTablesPort findFreeTablesPort() {
        return new FindFreeTablesJpaAdapter(tableRepository, tableBookingRepository);
    }

    @Bean
    public PersistBookingPort persistBookingPort() {
        return new PersistBookingJpaAdapter(bookingRepository);
    }

    @Bean
    public SaveBookingPort saveBookingPort() {
        return new SaveBookingJpaAdapter(bookingRepository, tableBookingRepository, tableRepository);
    }

    @Bean
    public FindTablesPort findTablesPort() {
        return new FindTablesJpaAdapter(tableRepository);
    }

    @Bean
    public FindBookingsPort findBookingsPort() {
        return new FindBookinsJpaAdapter(bookingRepository);
    }
}
