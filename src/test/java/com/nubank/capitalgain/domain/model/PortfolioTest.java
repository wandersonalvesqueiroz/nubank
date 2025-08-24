package com.nubank.capitalgain.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class PortfolioTest {

    private static final int QUANTITY_10 = 10;
    private static final int QUANTITY_4 = 4;
    private static final int QUANTITY_TOTAL = 20;
    private static final int CURRENT_QUANTITY_6 = 6;
    private static final BigDecimal UNIT_PRICE_5 = BigDecimal.valueOf(5.00);
    private static final BigDecimal UNIT_PRICE_7_5 = BigDecimal.valueOf(7.50);
    private static final BigDecimal UNIT_PRICE_10 = BigDecimal.valueOf(10.00);
    private static final BigDecimal UNIT_PRICE_15 = BigDecimal.valueOf(15.00);
    private static final BigDecimal WEIGHTED_AVERAGE = BigDecimal.valueOf(10.00);
    private static final BigDecimal ACCUMULATED_LOSS_50 = BigDecimal.valueOf(50.00);
    private static final BigDecimal ACCUMULATED_LOSS_80 = BigDecimal.valueOf(80.00);
    private static final BigDecimal ACCUMULATED_LOSS_100 = BigDecimal.valueOf(100.00);
    private static final BigDecimal ACCUMULATED_LOSS_150 = BigDecimal.valueOf(150.00);
    private static final BigDecimal PROFIT_100 = BigDecimal.valueOf(100.00);
    private static final BigDecimal DEDUCT_LOSS_20 = BigDecimal.valueOf(20.00);
    private static final BigDecimal DEDUCT_LOSS_100 = BigDecimal.valueOf(100.00);

    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
    }

    @Test
    @DisplayName("Deve calcular média ponderada corretamente após compra")
    void shouldCalculateWeightedAverageAfterBuy() {
        portfolio.buy(QUANTITY_10, UNIT_PRICE_5);
        portfolio.buy(QUANTITY_10, UNIT_PRICE_15);

        assertEquals(QUANTITY_TOTAL, portfolio.getCurrentQuantity());
        assertEquals(WEIGHTED_AVERAGE.setScale(2, RoundingMode.HALF_UP), portfolio.getWeightedAverage());
    }

    @Test
    @DisplayName("Deve reduzir quantidade ao vender")
    void shouldReduceQuantityOnSell() {
        portfolio.buy(QUANTITY_10, UNIT_PRICE_10);
        portfolio.sell(QUANTITY_4);

        assertEquals(CURRENT_QUANTITY_6, portfolio.getCurrentQuantity());
    }

    @Test
    @DisplayName("Deve acumular prejuízo corretamente")
    void shouldAccumulateLoss() {
        portfolio.accumulateLoss(ACCUMULATED_LOSS_100);
        portfolio.accumulateLoss(ACCUMULATED_LOSS_50);

        assertEquals(0, portfolio.getAccumulatedLoss().compareTo(ACCUMULATED_LOSS_150));
    }

    @Test
    @DisplayName("Deve deduzir prejuízo do lucro")
    void shouldDeductLossFromProfit() {
        portfolio.accumulateLoss(ACCUMULATED_LOSS_80);
        final var result = portfolio.deductLoss(PROFIT_100);

        assertEquals(DEDUCT_LOSS_20.setScale(2, RoundingMode.HALF_UP), result);
        assertEquals(0, portfolio.getAccumulatedLoss().compareTo(BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Deve manter lucro quando não há prejuízo acumulado")
    void shouldReturnProfitUnchangedWhenNoLoss() {
        final var result = portfolio.deductLoss(PROFIT_100);

        assertEquals(DEDUCT_LOSS_100.setScale(2, RoundingMode.HALF_UP), result);
    }

    @Test
    @DisplayName("Deve retornar null quando lucro é nulo")
    void shouldReturnNullWhenProfitIsNull() {
        assertNull(portfolio.deductLoss(null));
    }

    @Test
    @DisplayName("Deve retornar média ponderada como double")
    void shouldReturnWeightedAverageAsDouble() {
        portfolio.buy(QUANTITY_10, UNIT_PRICE_7_5);
        assertEquals(UNIT_PRICE_7_5.doubleValue(), portfolio.getWeightedAverageAsDouble());
    }

    @Test
    @DisplayName("Deve retornar prejuízo acumulado como double")
    void shouldReturnAccumulatedLossAsDouble() {
        portfolio.accumulateLoss(ACCUMULATED_LOSS_50);
        assertEquals(ACCUMULATED_LOSS_50.doubleValue(), portfolio.getAccumulatedLossAsDouble());
    }
}
