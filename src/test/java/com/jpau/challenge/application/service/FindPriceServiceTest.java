package com.jpau.challenge.application.service;

import com.jpau.challenge.application.exception.PriceNotFoundException;
import com.jpau.challenge.application.port.out.LoadPricesPort;
import com.jpau.challenge.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPriceServiceTest {

    private static final LocalDateTime DATE = LocalDateTime.of(2020, Month.JUNE, 14, 16, 0);
    private static final long PRODUCT_ID = 35455L;
    private static final long BRAND_ID = 1L;

    @Mock
    private LoadPricesPort loadPricesPort;

    private FindPriceService service;

    @BeforeEach
    void setUp() {
        service = new FindPriceService(loadPricesPort);
    }

    @Test
    void shouldReturnPriceWithHighestPriority() {
        // given
        Price standardPrice = generatePrice(1L, 0, "35.50");
        Price promotionalPrice = generatePrice(2L, 1, "25.45");
        when(loadPricesPort.loadPrices(DATE, PRODUCT_ID, BRAND_ID))
                .thenReturn(List.of(standardPrice, promotionalPrice));
        // when
        Price result = service.findPrice(DATE, PRODUCT_ID, BRAND_ID);
        // then
        assertThat(result).isEqualTo(promotionalPrice);
        verify(loadPricesPort).loadPrices(DATE, PRODUCT_ID, BRAND_ID);
    }

    @Test
    void shouldThrowExceptionWhenNoPriceExists() {
        // given
        when(loadPricesPort.loadPrices(DATE, PRODUCT_ID, BRAND_ID)).thenReturn(List.of());
        // when & then
        assertThatThrownBy(() -> service.findPrice(DATE, PRODUCT_ID, BRAND_ID))
                .isInstanceOf(PriceNotFoundException.class)
                .hasMessageContaining("35455")
                .hasMessageContaining("1");
    }

    private Price generatePrice(Long priceList, int priority, String amount) {
        return new Price(
                BRAND_ID,
                LocalDateTime.of(2020, Month.JUNE, 14, 0, 0),
                LocalDateTime.of(2020, Month.DECEMBER, 31, 23, 59, 59),
                priceList,
                PRODUCT_ID,
                priority,
                new BigDecimal(amount),
                "EUR"
        );
    }

}