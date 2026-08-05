package com.jpau.challenge.infrastructure.adapter.in.rest;

import com.jpau.challenge.application.port.in.FindPriceUseCase;
import com.jpau.challenge.domain.model.Price;
import com.jpau.challenge.generated.model.PriceResponse;
import com.jpau.challenge.infrastructure.adapter.in.rest.mapper.PriceRestMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceRestAdapterTest {

    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;

    @Mock
    private FindPriceUseCase findPriceUseCase;

    @Mock
    private PriceRestMapper mapper;

    @InjectMocks
    private PriceRestAdapter adapter;

    @Test
    void shouldReturnPrice() {
        // given
        String applicationDate = "2020-06-14-16.00.00";
        LocalDateTime parsedDate = LocalDateTime.of(2020, Month.JUNE, 14, 16, 0);
        Price price = mock(Price.class);
        PriceResponse response = new PriceResponse();
        when(findPriceUseCase.findPrice(parsedDate, PRODUCT_ID, BRAND_ID)).thenReturn(price);
        when(mapper.toApi(price)).thenReturn(response);
        // when
        ResponseEntity<PriceResponse> result = adapter.findPrice(applicationDate, PRODUCT_ID, BRAND_ID);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
        verify(findPriceUseCase).findPrice(parsedDate, PRODUCT_ID, BRAND_ID);
    }

    @Test
    void shouldThrowExceptionWhenDateIsInvalid() {
        // given
        String invalidDate = "2020/06/14 16:00";
        // when & then
        assertThatThrownBy(() -> adapter.findPrice(invalidDate, PRODUCT_ID, BRAND_ID))
                .isInstanceOf(DateTimeParseException.class);
        verifyNoInteractions(findPriceUseCase);
    }

}