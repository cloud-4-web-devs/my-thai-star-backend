package cloud4webdevs.mythaistar.booking.adapter.in.springweb;

import cloud4webdevs.mythaistar.booking.port.in.ConfirmBookingCommand;
import cloud4webdevs.mythaistar.booking.port.in.ConfirmBookingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("booking")
public class ConfirmBookingController {

  final ConfirmBookingUseCase createBookingUseCase;

  @PostMapping(value = "/booking/confirm/{token}")
  public void confirmBooking(@PathVariable String token) {
    final ConfirmBookingCommand createBookingCommand = mapInputToCommand(token);
    createBookingUseCase.confirm(createBookingCommand);
  }

  private ConfirmBookingCommand mapInputToCommand(String token) {

    return new ConfirmBookingCommand(token);
  }
}

