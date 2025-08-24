package com.nubank.capitalgain.domain.strategy.impl;

import com.nubank.capitalgain.domain.model.Portfolio;
import com.nubank.capitalgain.domain.strategy.TaxCalculationStrategy;
import com.nubank.capitalgain.domain.validation.OperationValidator;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.TaxResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.nubank.capitalgain.domain.constants.TaxConstants.*;
import static com.nubank.capitalgain.domain.util.Utils.*;
import static java.math.BigDecimal.ZERO;

@Component
@RequiredArgsConstructor
public class SellStrategy implements TaxCalculationStrategy {

    private final OperationValidator validator;

    @Override
    public boolean supports(OperationDTO operation, Portfolio portfolio) {
        return validator.isSell(operation.getOperation());
    }

    @Override
    public TaxResultDTO calculate(OperationDTO operation, Portfolio portfolio) {
        int quantity = operation.getQuantity();
        BigDecimal revenue = getDoubleMultiplyToInt(operation.getUnitCost(), quantity);
        BigDecimal costBasis = getBigDecimalMultiplyToInt(portfolio.getWeightedAverage(), quantity);
        BigDecimal profit = getSubtractHalfUp(revenue, costBasis);

        portfolio.sell(quantity);

        if (revenue.compareTo(TAX_FREE_LIMIT) <= 0 || profit.compareTo(ZERO) <= 0) {
            portfolio.accumulateLoss(profit.min(ZERO).abs());
            return TAX_RESULT_DTO_ZERO;
        }

        BigDecimal taxable = portfolio.deductLoss(profit);
        if (taxable.compareTo(ZERO) <= 0) {
            return TAX_RESULT_DTO_ZERO;
        }

        double tax = getBigDecimalMultiply(taxable, TAX_RATE).doubleValue();
        return new TaxResultDTO().tax(tax);
    }
}
