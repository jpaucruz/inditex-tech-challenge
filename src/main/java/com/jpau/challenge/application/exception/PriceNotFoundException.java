package com.jpau.challenge.application.exception;

import java.time.LocalDateTime;

public final class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(LocalDateTime date, Long productId, Long brandId) {
        super("No applicable price found for product %d, brand %d and date %s".formatted(productId, brandId, date));
    }

}