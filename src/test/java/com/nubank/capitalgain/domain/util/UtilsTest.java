package com.nubank.capitalgain.domain.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    private static final double DOUBLE_2_5 = 2.5;
    private static final int INT_4 = 4;
    private static final int INT_5 = 5;
    private static final BigDecimal BIG_DECIMAL_10 = BigDecimal.valueOf(10.0);
    private static final BigDecimal BIG_DECIMAL_3 = BigDecimal.valueOf(3);
    private static final BigDecimal BIG_DECIMAL_15 = BigDecimal.valueOf(15);
    private static final BigDecimal BIG_DECIMAL_2 = BigDecimal.valueOf(2);
    private static final BigDecimal BIG_DECIMAL_3_5 = BigDecimal.valueOf(3.5);
    private static final BigDecimal BIG_DECIMAL_7 = BigDecimal.valueOf(7.0);
    private static final BigDecimal BIG_DECIMAL_5_012 = BigDecimal.valueOf(5.012);
    private static final BigDecimal BIG_DECIMAL_10_123 = BigDecimal.valueOf(10.123);
    private static final BigDecimal BIG_DECIMAL_5_111 = BigDecimal.valueOf(5.111);

    @Test
    @DisplayName("Deve multiplicar double por int corretamente")
    void shouldMultiplyDoubleByIntCorrectly() {
        final var result = Utils.getDoubleMultiplyToInt(DOUBLE_2_5, INT_4);
        assertEquals(BIG_DECIMAL_10, result);
    }

    @Test
    @DisplayName("Deve multiplicar BigDecimal por int corretamente")
    void shouldMultiplyBigDecimalByIntCorrectly() {
        final var result = Utils.getBigDecimalMultiplyToInt(BIG_DECIMAL_3, INT_5);
        assertEquals(BIG_DECIMAL_15, result);
    }

    @Test
    @DisplayName("Deve multiplicar dois BigDecimal corretamente")
    void shouldMultiplyTwoBigDecimalsCorrectly() {
        BigDecimal result = Utils.getBigDecimalMultiply(BIG_DECIMAL_2, BIG_DECIMAL_3_5);
        assertEquals(BIG_DECIMAL_7, result);
    }

    @Test
    @DisplayName("Deve subtrair e aplicar arredondamento HALF_UP")
    void shouldSubtractAndRoundHalfUp() {
        final var result = Utils.getSubtractHalfUp(BIG_DECIMAL_10_123, BIG_DECIMAL_5_111);
        final var expected = BIG_DECIMAL_5_012.setScale(2, RoundingMode.HALF_UP);
        assertEquals(expected, result);
    }
}
