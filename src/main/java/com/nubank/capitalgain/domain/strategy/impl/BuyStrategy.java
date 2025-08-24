package com.nubank.capitalgain.domain.strategy.impl;

import com.nubank.capitalgain.domain.model.Portfolio;
import com.nubank.capitalgain.domain.strategy.TaxCalculationStrategy;
import com.nubank.capitalgain.domain.validation.OperationValidator;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.TaxResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static com.nubank.capitalgain.domain.constants.TaxConstants.TAX_ZERO;

@Component
@RequiredArgsConstructor
public class BuyStrategy implements TaxCalculationStrategy {

    private final OperationValidator operationValidator;

    @Override
    public boolean supports(OperationDTO operation, Portfolio portfolio) {
        return operationValidator.isBuy(operation.getOperation());
    }

    @Override
    public TaxResultDTO calculate(OperationDTO operation, Portfolio portfolio) {
        portfolio.buy(operation.getQuantity(), BigDecimal.valueOf(operation.getUnitCost()));
        return new TaxResultDTO().tax(TAX_ZERO);
    }
}

