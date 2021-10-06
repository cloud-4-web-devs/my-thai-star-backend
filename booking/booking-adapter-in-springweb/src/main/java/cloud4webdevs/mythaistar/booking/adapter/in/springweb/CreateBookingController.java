package cloud4webdevs.mythaistar.booking.adapter.in.springweb;

import cloud4webdevs.mythaistar.booking.domain.TableId;
import cloud4webdevs.mythaistar.booking.port.in.CreateBookingCommand;
import cloud4webdevs.mythaistar.booking.port.in.CreateBookingResult;
import cloud4webdevs.mythaistar.booking.port.in.CreateBookingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("booking")
public class CreateBookingController {

  final CreateBookingUseCase createBookingUseCase;

  @PostMapping(value = "/booking")
  public CreateBookingResult createBooking(@RequestBody CreateBookingResource createBookingResource) {
    final CreateBookingCommand createBookingCommand = mapInputToCommand(createBookingResource);
    return createBookingUseCase.createBooking(createBookingCommand);
  }

  private CreateBookingCommand mapInputToCommand(CreateBookingResource createBookingResource) {
    TableId suggestedTable = Optional.ofNullable(createBookingResource.getSuggestedTable())
        .map(TableId::new)
        .orElse(null);

    return new CreateBookingCommand(
        createBookingResource.getBookingFrom(),
        createBookingResource.getBookingTo(),
        createBookingResource.getEmail(),
        createBookingResource.getSeatsNumber(),
        suggestedTable);
  }
}

