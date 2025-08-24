package com.nubank.capitalgain.domain.service;

import com.nubank.capitalgain.domain.model.Portfolio;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.TaxResultDTO;

public interface TaxCalculatorService {
    TaxResultDTO calculate(OperationDTO operation, Portfolio portfolio);
}