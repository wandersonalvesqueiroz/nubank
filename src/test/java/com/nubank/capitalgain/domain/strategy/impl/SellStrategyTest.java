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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SellStrategyTest {

    private static final int QUANTITY = 10;
    private static final double TAX_ZERO = 0.0;
    private static final double UNIT_COST_5 = 5.0;
    private static final double UNIT_COST_10 = 10.0;
    private static final double UNIT_COST_15 = 15.0;
    private static final double UNIT_COST_20 = 20.0;
    private static final double UNIT_COST_30000 = 30000.0;
    private static final BigDecimal TAX_RATE = new BigDecimal("0.20");
    private static final BigDecimal WEIGHTED_AVERAGE = BigDecimal.valueOf(10.0);
    private static final BigDecimal ACCUMULATE_LOSS_100 = BigDecimal.valueOf(100.00);
    private static final BigDecimal ACCUMULATE_LOSS_50 = BigDecimal.valueOf(50.00);

    @Mock
    private OperationValidator validator;

    @InjectMocks
    private SellStrategy sellStrategy;

    private OperationDTO operation;
    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        operation = new OperationDTO();
        operation.setOperation(OperationType.SELL);
        operation.setQuantity(QUANTITY);
        operation.setUnitCost(UNIT_COST_20);

        portfolio = new Portfolio();
        portfolio.buy(QUANTITY, WEIGHTED_AVERAGE);
    }

    @Test
    @DisplayName("Deve suportar operação de venda")
    void shouldSupportSellOperation() {
        when(validator.isSell(OperationType.SELL))
                .thenReturn(true);

        boolean result = sellStrategy.supports(operation, portfolio);

        assertTrue(result);
        verify(validator).isSell(OperationType.SELL);
    }

    @Test
    @DisplayName("Deve calcular imposto sobre lucro acima do limite de isenção")
    void shouldCalculateTaxWhenProfitAboveLimit() {
        operation.setUnitCost(UNIT_COST_30000);

        final var result = sellStrategy.calculate(operation, portfolio);

        final var revenue = BigDecimal.valueOf(UNIT_COST_30000 * QUANTITY);
        final var costBasis = WEIGHTED_AVERAGE.multiply(BigDecimal.valueOf(QUANTITY));
        final var profit = revenue.subtract(costBasis).setScale(2, RoundingMode.HALF_UP);
        final var expectedTax = profit.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedTax.doubleValue(), result.getTax());
    }

    @Test
    @DisplayName("Deve retornar imposto zero quando lucro abaixo do limite de isenção")
    void shouldReturnZeroTaxWhenRevenueBelowLimit() {
        operation.setUnitCost(UNIT_COST_10);

        final var result = sellStrategy.calculate(operation, portfolio);

        assertEquals(TAX_ZERO, result.getTax());
        assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), portfolio.getAccumulatedLoss());
    }

    @Test
    @DisplayName("Deve acumular prejuízo e retornar imposto zero")
    void shouldAccumulateLossWhenNegativeProfit() {
        operation.setUnitCost(UNIT_COST_5);

        final var result = sellStrategy.calculate(operation, portfolio);

        assertEquals(TAX_ZERO, result.getTax());
        assertEquals(ACCUMULATE_LOSS_50.setScale(2, RoundingMode.HALF_UP), portfolio.getAccumulatedLoss());
    }

    @Test
    @DisplayName("Deve compensar prejuízo acumulado e retornar imposto zero")
    void shouldDeductLossAndReturnZeroTax() {
        portfolio.accumulateLoss(ACCUMULATE_LOSS_50);
        operation.setUnitCost(UNIT_COST_15);

        final var result = sellStrategy.calculate(operation, portfolio);

        assertEquals(TAX_ZERO, result.getTax());
        assertEquals(0, ACCUMULATE_LOSS_50.compareTo(portfolio.getAccumulatedLoss()));
    }
}
