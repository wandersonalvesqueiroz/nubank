package com.nubank.capitalgain.domain.strategy.impl;

import com.nubank.capitalgain.domain.model.Portfolio;
import com.nubank.capitalgain.domain.validation.OperationValidator;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.nubank.capitalgain.domain.constants.TaxConstants.TAX_ZERO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyStrategyTest {

    private static final int QUANTITY = 10;
    private static final double UNIT_COST = 5.0;

    @Mock
    private OperationValidator operationValidator;

    @InjectMocks
    private BuyStrategy buyStrategy;

    private OperationDTO operation;
    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        buyStrategy = new BuyStrategy(operationValidator);

        operation = new OperationDTO();
        operation.setOperation(OperationType.BUY);
        operation.setQuantity(QUANTITY);
        operation.setUnitCost(UNIT_COST);

        portfolio = new Portfolio();
    }

    @Test
    @DisplayName("Deve suportar operação de compra")
    void shouldSupportBuyOperation() {
        when(operationValidator.isBuy(OperationType.BUY))
                .thenReturn(true);

        final var result = buyStrategy.supports(operation, portfolio);

        assertTrue(result);
        verify(operationValidator).isBuy(OperationType.BUY);
    }

    @Test
    @DisplayName("Não deve suportar operação que não seja de compra")
    void shouldNotSupportNonBuyOperation() {
        operation.setOperation(OperationType.SELL);
        when(operationValidator.isBuy(OperationType.SELL))
                .thenReturn(false);

        final var result = buyStrategy.supports(operation, portfolio);

        assertFalse(result);
        verify(operationValidator).isBuy(OperationType.SELL);
    }

    @Test
    @DisplayName("Deve calcular imposto como zero e atualizar o portfolio")
    void shouldCalculateTaxAsZeroAndUpdatePortfolio() {
        final var result = buyStrategy.calculate(operation, portfolio);

        assertEquals(TAX_ZERO, result.getTax());
        assertEquals(QUANTITY, portfolio.getCurrentQuantity());
        assertEquals(BigDecimal.valueOf(UNIT_COST).setScale(2, RoundingMode.HALF_UP), portfolio.getWeightedAverage());
    }
}
