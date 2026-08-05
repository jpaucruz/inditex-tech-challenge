package com.jpau.challenge.infrastructure.adapter.out.persistence;

import com.jpau.challenge.application.port.out.LoadPricesPort;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest(properties = "spring.sql.init.mode=never")
@Sql("/db/integration-data-test.sql")
class PricePersistenceAdapterIntegrationTest {

    private static final Long PRODUCT_ID = 98765L;
    private static final Long BRAND_ID = 7L;

    @Autowired
    private PriceRepository repository;

    private LoadPricesPort loadPricesPort;

    @BeforeEach
    void setUp() {
        PricePersistenceMapper mapper = Mappers.getMapper(PricePersistenceMapper.class);
        loadPricesPort = new PricePersistenceAdapter(repository, mapper);
    }

    @Test
    void shouldLoadAllPricesAtRequestedDate() {
        // given
        LocalDateTime date = LocalDateTime.of(2024, Month.MARCH, 10, 16, 0);
        // when
        List<Price> result = loadPricesPort.loadPrices(date,PRODUCT_ID,BRAND_ID);
        // then
        assertThat(result).extracting(Price::priceList).containsExactlyInAnyOrder(101L, 102L);
    }

    @Test
    void shouldIncludeValidityRangeBoundaries() {
        // given
        LocalDateTime date = LocalDateTime.of(2024, Month.MARCH, 10, 18, 30);
        // when
        List<Price> result = loadPricesPort.loadPrices(date, PRODUCT_ID, BRAND_ID);
        // then
        assertThat(result).extracting(Price::priceList) .containsExactlyInAnyOrder(101L, 102L);
    }

    @Test
    void shouldReturnEmptyListWhenNoPriceExists() {
        // given
        LocalDateTime date = LocalDateTime.of(2024, Month.MARCH, 10, 16, 0);
        // when
        List<Price> result = loadPricesPort.loadPrices(date,99999L, BRAND_ID);
        // then
        assertThat(result).isEmpty();
    }

}