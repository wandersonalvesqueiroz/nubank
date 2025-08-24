package com.nubank.capitalgain.domain.strategy.config;

import com.nubank.capitalgain.domain.strategy.TaxCalculationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StrategyConfig {

    @Bean
    public List<TaxCalculationStrategy> taxStrategies(List<TaxCalculationStrategy> strategies) {
        return strategies;
    }
}
