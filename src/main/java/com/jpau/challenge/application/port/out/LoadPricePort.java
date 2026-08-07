package com.jpau.challenge.application.port.out;

import com.jpau.challenge.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LoadPricePort {

    Optional<Price> findPrice(LocalDateTime date, Long productId, Long brandId);

}