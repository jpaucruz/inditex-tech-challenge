package com.jpau.challenge.application.service;

import com.jpau.challenge.application.exception.PriceNotFoundException;
import com.jpau.challenge.application.port.in.FindPriceUseCase;
import com.jpau.challenge.application.port.out.LoadPricePort;
import com.jpau.challenge.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Objects;

public final class FindPriceService
        implements FindPriceUseCase {

    private final LoadPricePort loadPricePort;

    public FindPriceService(LoadPricePort loadPricePort) {
        this.loadPricePort = Objects.requireNonNull(loadPricePort,"loadPricePort must not be null");
    }

    @Override
    public Price findPrice(LocalDateTime date, Long productId, Long brandId) {
        // we assume that you always have to filter through these fields
        Objects.requireNonNull(date,"date must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(brandId, "brandId must not be null");
        return loadPricePort
                .findPrice(date, productId, brandId)
                .orElseThrow(() -> new PriceNotFoundException(date, productId, brandId));
    }

}