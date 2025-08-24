package com.nubank.capitalgain.domain.service.impl;

import com.nubank.capitalgain.domain.model.Portfolio;
import com.nubank.capitalgain.domain.service.TaxCalculatorService;
import com.nubank.capitalgain.domain.strategy.TaxCalculationStrategy;
import com.nubank.capitalgain.exception.InvalidOperationException;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.TaxResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.nubank.capitalgain.domain.constants.MessageConstants.ERROR_OPERATION;
import static com.nubank.capitalgain.domain.validation.OperationValidator.validateOperation;

@Service
@RequiredArgsConstructor
public class TaxCalculatorServiceImpl implements TaxCalculatorService {

    private final List<TaxCalculationStrategy> strategies;

    @Override
    public TaxResultDTO calculate(OperationDTO operation, Portfolio portfolio) {
        validateOperation(operation);

        return strategies.stream()
                .filter(s -> s.supports(operation, portfolio))
                .findFirst()
                .orElseThrow(() -> new InvalidOperationException(ERROR_OPERATION))
                .calculate(operation, portfolio);
    }
}
