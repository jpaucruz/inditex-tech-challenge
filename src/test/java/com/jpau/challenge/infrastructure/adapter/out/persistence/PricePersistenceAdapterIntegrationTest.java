package com.jpau.challenge.infrastructure.adapter.out.persistence;

import com.jpau.challenge.application.port.out.LoadPricePort;
import com.jpau.challenge.domain.model.Price;
import com.jpau.challenge.infrastructure.adapter.out.persistence.mapper.PricePersistenceMapper;
import com.jpau.challenge.infrastructure.adapter.out.persistence.repository.PriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest(properties = "spring.sql.init.mode=never")
@Sql("/db/integration-data-test.sql")
class PricePersistenceAdapterIntegrationTest {

    private static final Long PRODUCT_ID = 98765L;
    private static final Long BRAND_ID = 7L;

    @Autowired
    private PriceRepository repository;

    private LoadPricePort loadPricePort;

    @BeforeEach
    void setUp() {
        PricePersistenceMapper mapper = Mappers.getMapper(PricePersistenceMapper.class);
        loadPricePort = new PricePersistenceAdapter(repository, mapper);
    }

    @Test
    void shouldLoadPriceWithHighestPriority() {
        // given
        LocalDateTime date = LocalDateTime.of(2024, Month.MARCH, 10, 16, 0);
        // when
        Optional<Price> result = loadPricePort.findPrice(date,PRODUCT_ID,BRAND_ID);
        // then
        assertThat(result).isPresent();
    }

    @Test
    void shouldIncludeValidityRangeBoundaries() {
        // given
        LocalDateTime date = LocalDateTime.of(2024, Month.MARCH, 10, 18, 30);
        // when
        Optional<Price> result = loadPricePort.findPrice(date, PRODUCT_ID, BRAND_ID);
        // then
        assertThat(result).isPresent();
        assertThat(result.orElseThrow().priceList()).isEqualTo(102L);
    }

    @Test
    void shouldReturnEmptyWhenNoPriceExists() {
        // given
        LocalDateTime date = LocalDateTime.of(2024, Month.MARCH, 10, 16, 0);
        // when
        Optional<Price> result = loadPricePort.findPrice(date,99999L, BRAND_ID);
        // then
        assertThat(result).isEmpty();
    }

}