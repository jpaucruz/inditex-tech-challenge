package com.jpau.challenge.infrastructure.adapter.out.persistence.mapper;

import com.jpau.challenge.domain.model.Price;
import com.jpau.challenge.infrastructure.adapter.out.persistence.entity.PriceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PricePersistenceMapper {

    @Mapping(target = "amount", source = "price")
    Price toDomain(PriceEntity source);

}