package cloud4webdevs.mythaistar.booking.adapter.in.springweb;

import cloud4webdevs.mythaistar.common.domain.exception.NotFoundException;
import cloud4webdevs.mythaistar.booking.domain.exception.BookingValidationException;
import cloud4webdevs.mythaistar.booking.domain.exception.IllegalBookingStateException;
import cloud4webdevs.mythaistar.booking.domain.exception.IncompleteBookingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<String> handle(NotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(BookingValidationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(IncompleteBookingException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(IllegalBookingStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
