package com.jpau.challenge.application.service;

import com.jpau.challenge.application.exception.PriceNotFoundException;
import com.jpau.challenge.application.port.in.FindPriceUseCase;
import com.jpau.challenge.application.port.out.LoadPricesPort;
import com.jpau.challenge.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

public final class FindPriceService
        implements FindPriceUseCase {

    private final LoadPricesPort loadPricesPort;

    public FindPriceService(LoadPricesPort loadPricesPort) {
        this.loadPricesPort = Objects.requireNonNull(loadPricesPort,"loadPricesPort must not be null");
    }

    @Override
    public Price findPrice(LocalDateTime date, Long productId, Long brandId) {
        // we assume that you always have to filter through these fields
        Objects.requireNonNull(date,"date must not be null");
        Objects.requireNonNull(productId, "productId must not be null");
        Objects.requireNonNull(brandId, "brandId must not be null");
        return loadPricesPort
                .loadPrices(date, productId, brandId)
                .stream()
                .max(Comparator.comparingInt(Price::priority))
                .orElseThrow(() -> new PriceNotFoundException(date, productId, brandId));
    }

}