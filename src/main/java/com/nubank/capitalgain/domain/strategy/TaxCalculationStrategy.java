package com.nubank.capitalgain.domain.strategy;

import com.nubank.capitalgain.domain.model.Portfolio;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.TaxResultDTO;

public interface TaxCalculationStrategy {
    boolean supports(OperationDTO operation, Portfolio portfolio);

    TaxResultDTO calculate(OperationDTO operation, Portfolio portfolio);
}
