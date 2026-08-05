package com.jpau.challenge.infrastructure.adapter.in.rest.mapper;

import com.jpau.challenge.domain.model.Price;
import com.jpau.challenge.generated.model.PriceResponse;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PriceRestMapper {

    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd-HH.mm.ss");

    @Mapping(target = "startDate", source = "startDate", qualifiedByName = "formatDate")
    @Mapping(target = "endDate", source = "endDate", qualifiedByName = "formatDate")
    @Mapping(target = "price", source = "amount")
    PriceResponse toApi(Price source);

    @Named("formatDate")
    default String formatDate(LocalDateTime date) {
        return date.format(DATE_FORMATTER);
    }

}