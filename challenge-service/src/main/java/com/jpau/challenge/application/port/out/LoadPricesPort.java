package com.jpau.challenge.application.port.out;

import com.jpau.challenge.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface LoadPricesPort {

    List<Price> loadPrices(LocalDateTime date, Long productId, Long brandId);

}