package com.jpau.challenge.application.port.in;

import com.jpau.challenge.domain.model.Price;

import java.time.LocalDateTime;

public interface FindPriceUseCase {

    Price findPrice(LocalDateTime date, Long productId, Long brandId);

}