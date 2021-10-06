package cloud4webdevs.mythaistar.springboot.startup;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity.TableEntity;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Creates some tables on app's startup if tables table is empty.
 *
 * Maybe we need a use case for this? But this is in fact a workaround for missing table management feature.
 */
@AllArgsConstructor
@Service
public class BootstrapDataOnStartup {

    private final TableRepository tableRepository;

    @PostConstruct
    @Transactional
    public void init() {
        final long tableCount = tableRepository.count();
        if (tableCount == 0) {
            List.of(2, 2, 4, 5, 6, 6, 8, 12).forEach(maxSeats -> {
                final TableEntity tableEntity = new TableEntity();
                tableEntity.setMaxSeats(maxSeats);
                tableRepository.save(tableEntity);
            });
        }
    }
}
