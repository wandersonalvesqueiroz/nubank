package com.nubank.capitalgain.domain.util;

import java.math.BigDecimal;

import static com.nubank.capitalgain.domain.constants.TaxConstants.ROUNDING_MODE_UP;
import static com.nubank.capitalgain.domain.constants.TaxConstants.SCALE;

public class Utils {

    public static BigDecimal getDoubleMultiplyToInt(double valueOne, int valueTwo) {
        return getBigDecimalMultiplyToInt(BigDecimal.valueOf(valueOne), valueTwo);
    }

    public static BigDecimal getBigDecimalMultiplyToInt(BigDecimal valueOne, int valueTwo) {
        return getBigDecimalMultiply(valueOne, BigDecimal.valueOf(valueTwo));
    }

    public static BigDecimal getBigDecimalMultiply(BigDecimal valueOne, BigDecimal valueTwo) {
        return valueOne.multiply(valueTwo);
    }

    public static BigDecimal getSubtractHalfUp(BigDecimal valueOne, BigDecimal valueTwo) {
        return valueOne.subtract(valueTwo).setScale(SCALE, ROUNDING_MODE_UP);
    }
}
