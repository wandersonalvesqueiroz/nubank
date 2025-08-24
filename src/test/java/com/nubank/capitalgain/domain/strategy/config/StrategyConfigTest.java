package com.nubank.capitalgain.domain.strategy.config;

import com.nubank.capitalgain.domain.strategy.TaxCalculationStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StrategyConfigTest {

    private static final String NAME = "taxStrategies";

    @Mock
    private TaxCalculationStrategy taxCalculationStrategy;

    @Test
    @DisplayName("Deve registrar o bean taxStrategies com pelo menos uma estratégia")
    void shouldRegisterTaxStrategiesBeanWithStrategies() {
        final var context = new AnnotationConfigApplicationContext();
        context.registerBean(NAME, TaxCalculationStrategy.class, () -> taxCalculationStrategy);
        context.register(StrategyConfig.class);
        context.refresh();

        final var strategies = context.getBean(NAME, List.class);

        assertNotNull(strategies);
        assertFalse(strategies.isEmpty());

        for (Object strategy : strategies) {
            assertNotNull(strategy);
            assertInstanceOf(TaxCalculationStrategy.class, strategy);
        }

        context.close();
    }

    @Test
    @DisplayName("Deve permitir a injeção de estratégias personalizadas no contexto")
    void shouldAllowCustomStrategyInjection() {
        final var context = new AnnotationConfigApplicationContext();
        context.registerBean(TaxCalculationStrategy.class, () -> taxCalculationStrategy);
        context.register(StrategyConfig.class);
        context.refresh();

        final var strategies = context.getBean(NAME, List.class);

        assertTrue(strategies.contains(taxCalculationStrategy));

        context.close();
    }
}
