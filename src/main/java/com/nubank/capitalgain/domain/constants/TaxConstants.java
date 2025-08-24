package com.nubank.capitalgain.domain.constants;

import com.nubank.capitalgain.generated.model.TaxResultDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class TaxConstants {
    public static final BigDecimal TAX_RATE = new BigDecimal("0.20");
    public static final BigDecimal TAX_FREE_LIMIT = new BigDecimal("20000.00");
    public static final int SCALE = 2;
    public static final RoundingMode ROUNDING_MODE_UP = RoundingMode.HALF_UP;
    public static final double TAX_ZERO = 0.0;
    public static final TaxResultDTO TAX_RESULT_DTO_ZERO = new TaxResultDTO().tax(TAX_ZERO);
}
