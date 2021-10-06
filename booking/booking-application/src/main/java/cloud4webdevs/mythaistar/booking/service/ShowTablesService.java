package cloud4webdevs.mythaistar.booking.service;

import cloud4webdevs.mythaistar.booking.port.in.ShowTablesQuery;
import cloud4webdevs.mythaistar.booking.port.in.ShowTablesResult;
import cloud4webdevs.mythaistar.booking.port.in.ShowTablesUseCase;
import cloud4webdevs.mythaistar.booking.port.out.FindTablesPort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ShowTablesService implements ShowTablesUseCase {

    private final FindTablesPort findTablesPort;

    @Override
    public List<ShowTablesResult> showTables(ShowTablesQuery showTablesQuery) {
        return findTablesPort.findTables().stream()
                .map(table -> new ShowTablesResult(table.getId().getValue(), table.getMaxSeats()))
                .collect(Collectors.toList());
    }
}
