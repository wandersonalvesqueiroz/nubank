package com.nubank.capitalgain.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

import static com.nubank.capitalgain.domain.constants.TaxConstants.ROUNDING_MODE_UP;
import static com.nubank.capitalgain.domain.constants.TaxConstants.SCALE;
import static java.math.BigDecimal.ZERO;

@Getter
public class Portfolio {
    private int currentQuantity = 0;
    private BigDecimal weightedAverage = ZERO;
    private BigDecimal accumulatedLoss = ZERO;

    public void buy(int quantity, BigDecimal unitPrice) {
        BigDecimal currentTotal = weightedAverage.multiply(BigDecimal.valueOf(currentQuantity));
        BigDecimal newTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal combinedTotal = currentTotal.add(newTotal);
        currentQuantity += quantity;

        if (currentQuantity > 0) {
            weightedAverage = combinedTotal.divide(BigDecimal.valueOf(currentQuantity), SCALE, ROUNDING_MODE_UP);
        }
    }

    public void sell(int quantity) {
        currentQuantity -= quantity;
    }

    public void accumulateLoss(BigDecimal value) {
        accumulatedLoss = accumulatedLoss.add(value);
    }

    public BigDecimal deductLoss(BigDecimal profit) {
        if (profit == null) return null;

        if (accumulatedLoss.compareTo(ZERO) > 0) {
            BigDecimal deduction = profit.min(accumulatedLoss);
            accumulatedLoss = accumulatedLoss.subtract(deduction);
            return profit.subtract(deduction).setScale(SCALE, ROUNDING_MODE_UP);
        }
        return profit.setScale(SCALE, ROUNDING_MODE_UP);
    }

    public Double getWeightedAverageAsDouble() {
        return weightedAverage.setScale(SCALE, ROUNDING_MODE_UP).doubleValue();
    }

    public Double getAccumulatedLossAsDouble() {
        return accumulatedLoss.setScale(SCALE, ROUNDING_MODE_UP).doubleValue();
    }
}
