package cloud4webdevs.mythaistar.booking.adapter.out.jpa;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.mapper.TableMapper;
import cloud4webdevs.mythaistar.booking.adapter.out.jpa.repository.TableRepository;
import cloud4webdevs.mythaistar.booking.domain.Table;
import cloud4webdevs.mythaistar.booking.port.out.FindTablesPort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class FindTablesJpaAdapter implements FindTablesPort {

    private final TableRepository tableRepository;

    private final TableMapper tableMapper = TableMapper.INSTANCE;

    @Override
    public List<Table> findTables() {
        return tableRepository.findAll()
            .stream()
            .map(tableMapper::toDomain)
            .collect(Collectors.toList());
    }
}
