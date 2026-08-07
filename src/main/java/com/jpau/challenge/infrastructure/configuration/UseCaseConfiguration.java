package com.jpau.challenge.infrastructure.configuration;

import com.jpau.challenge.application.port.in.FindPriceUseCase;
import com.jpau.challenge.application.port.out.LoadPricePort;
import com.jpau.challenge.application.service.FindPriceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    FindPriceUseCase findPriceUseCase(LoadPricePort loadPricePort) {
        return new FindPriceService(loadPricePort);
    }

}