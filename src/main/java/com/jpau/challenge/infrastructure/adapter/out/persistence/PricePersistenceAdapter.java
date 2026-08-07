package com.jpau.challenge.infrastructure.adapter.out.persistence;

import com.jpau.challenge.application.port.out.LoadPricePort;
import com.jpau.challenge.domain.model.Price;
import com.jpau.challenge.infrastructure.adapter.out.persistence.mapper.PricePersistenceMapper;
import com.jpau.challenge.infrastructure.adapter.out.persistence.repository.PriceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Transactional(readOnly = true)
public class PricePersistenceAdapter implements LoadPricePort {

    private final PriceRepository repository;
    private final PricePersistenceMapper mapper;

    public PricePersistenceAdapter(PriceRepository repository, PricePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Price> findPrice(LocalDateTime date, Long productId, Long brandId) {
        return repository.findPrice(date, productId, brandId)
                .map(mapper::toDomain);
    }
}