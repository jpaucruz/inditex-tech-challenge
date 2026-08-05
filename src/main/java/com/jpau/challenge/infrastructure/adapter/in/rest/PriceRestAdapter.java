package com.jpau.challenge.infrastructure.adapter.in.rest;

import com.jpau.challenge.application.port.in.FindPriceUseCase;
import com.jpau.challenge.generated.api.PricesApi;
import com.jpau.challenge.generated.model.PriceResponse;
import com.jpau.challenge.infrastructure.adapter.in.rest.mapper.PriceRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

@RestController
public class PriceRestAdapter implements PricesApi {

    private static final DateTimeFormatter APPLICATION_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd-HH.mm.ss").withResolverStyle(ResolverStyle.STRICT);

    private final FindPriceUseCase findPriceUseCase;
    private final PriceRestMapper mapper;

    public PriceRestAdapter(FindPriceUseCase findPriceUseCase, PriceRestMapper mapper) {
        this.findPriceUseCase = findPriceUseCase;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<PriceResponse> findPrice(String date, Long productId, Long brandId) {
        LocalDateTime parsedDate = LocalDateTime.parse(date, APPLICATION_DATE_FORMATTER);
        return ResponseEntity.ok(mapper.toApi(findPriceUseCase.findPrice(parsedDate, productId, brandId)));
    }

}