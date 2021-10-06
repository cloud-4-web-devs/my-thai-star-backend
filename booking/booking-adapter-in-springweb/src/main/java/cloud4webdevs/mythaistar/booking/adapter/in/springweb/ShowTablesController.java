package cloud4webdevs.mythaistar.booking.adapter.in.springweb;

import cloud4webdevs.mythaistar.booking.port.in.ShowTablesQuery;
import cloud4webdevs.mythaistar.booking.port.in.ShowTablesResult;
import cloud4webdevs.mythaistar.booking.port.in.ShowTablesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("booking")
public class ShowTablesController {

    private final ShowTablesUseCase showTablesUseCase;

    @GetMapping(value = "/tables")
    public List<ShowTablesResult> showTables() {
        // in the future, ShowTablesQuery may contain something interesting
        return showTablesUseCase.showTables(new ShowTablesQuery());
    }
}
