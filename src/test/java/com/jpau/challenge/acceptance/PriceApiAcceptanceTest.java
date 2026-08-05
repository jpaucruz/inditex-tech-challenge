package com.jpau.challenge.acceptance;

import com.jpau.challenge.generated.model.ErrorResponse;
import com.jpau.challenge.generated.model.PriceResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = "spring.sql.init.mode=never")
@AutoConfigureTestRestTemplate
@Sql("/db/acceptance-data-test.sql")
class PriceApiAcceptanceTest {

    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    // it generates independent executions.
    // we avoid duplicating the preparation, the HTTP call, and the validations, but the scenarios appear individually in the JUnit report.
    @ParameterizedTest(name = "[{index}] date={0} should return price list {1}")
    @CsvSource({
            "2020-06-14-10.00.00, 1, 35.50",
            "2020-06-14-16.00.00, 2, 25.45",
            "2020-06-14-21.00.00, 1, 35.50",
            "2020-06-15-10.00.00, 3, 30.50",
            "2020-06-16-21.00.00, 4, 38.95"
    })
    void shouldReturnPrice(String date, Long expectedPriceList, String expectedPrice) {
        // given
        URI uri = buildUri(date, PRODUCT_ID, BRAND_ID);
        // when
        ResponseEntity<PriceResponse> response = restTemplate.getForEntity(uri, PriceResponse.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        PriceResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getProductId()).isEqualTo(PRODUCT_ID);
        assertThat(body.getBrandId()).isEqualTo(BRAND_ID);
        assertThat(body.getPriceList()).isEqualTo(expectedPriceList);
        assertThat(body.getPrice()).isEqualByComparingTo(new BigDecimal(expectedPrice));
        assertThat(body.getCurrency()).isEqualTo("EUR");
    }

    @Test
    void shouldReturnBadRequestWhenRequiredParameterIsMissing() {
        // given
        URI uri = UriComponentsBuilder
                .fromUriString(baseUrl())
                .path("/api/v1/prices")
                .queryParam("date","2020-06-14-16.00.00")
                .queryParam("productId", PRODUCT_ID)
                .build()
                .toUri();
        // when
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(uri, ErrorResponse.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_REQUEST");
    }

    @Test
    void shouldReturnBadRequestWhenDateIsInvalid() {
        // given
        URI uri = buildUri("2020/06/14 16:00", PRODUCT_ID, BRAND_ID);
        // when
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(uri, ErrorResponse.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("INVALID_REQUEST");
    }

    @Test
    void shouldReturnNotFoundWhenNoApplicablePriceExists() {
        // given
        URI uri = buildUri("2020-06-14-16.00.00",99999L, BRAND_ID);
        // when
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(uri, ErrorResponse.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("PRICE_NOT_FOUND");
    }

    private URI buildUri(String date, Long productId, Long brandId) {
        return UriComponentsBuilder
                .fromUriString(baseUrl())
                .path("/api/v1/prices")
                .queryParam("date", date)
                .queryParam("productId", productId)
                .queryParam("brandId", brandId)
                .build()
                .encode()
                .toUri();
    }

    private String baseUrl() {
        return "http://localhost:" + port;
    }

}