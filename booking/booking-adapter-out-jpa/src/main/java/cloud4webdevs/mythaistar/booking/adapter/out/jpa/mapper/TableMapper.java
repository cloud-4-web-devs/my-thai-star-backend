package cloud4webdevs.mythaistar.booking.adapter.out.jpa.mapper;

import cloud4webdevs.mythaistar.booking.adapter.out.jpa.entity.TableEntity;
import cloud4webdevs.mythaistar.booking.domain.Table;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TableMapper {

    TableMapper INSTANCE = Mappers.getMapper(TableMapper.class);

    @Mapping(target="id.value", source="id")
    Table toDomain(TableEntity entity);
}
