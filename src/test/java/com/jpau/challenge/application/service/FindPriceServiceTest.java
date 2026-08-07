package com.jpau.challenge.application.service;

import com.jpau.challenge.application.exception.PriceNotFoundException;
import com.jpau.challenge.application.port.out.LoadPricePort;
import com.jpau.challenge.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

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
    private LoadPricePort loadPricePort;

    private FindPriceService service;

    @BeforeEach
    void setUp() {
        service = new FindPriceService(loadPricePort);
    }

    @Test
    void shouldReturnPriceWhenPriceExists() {
        // given
        Price expectedPrice = generatePrice(2L, 1, "25.45");
        when(loadPricePort.findPrice(DATE, PRODUCT_ID, BRAND_ID))
                .thenReturn(Optional.of(expectedPrice));
        // when
        Price result = service.findPrice(DATE, PRODUCT_ID, BRAND_ID);
        // then
        assertThat(result).isEqualTo(expectedPrice);
        verify(loadPricePort).findPrice(DATE, PRODUCT_ID, BRAND_ID);
    }

    @Test
    void shouldThrowExceptionWhenNoPriceExists() {
        // given
        when(loadPricePort.findPrice(DATE, PRODUCT_ID, BRAND_ID)).thenReturn(Optional.empty());
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