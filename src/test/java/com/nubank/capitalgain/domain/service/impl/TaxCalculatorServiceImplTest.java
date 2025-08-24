package com.nubank.capitalgain.domain.service.impl;

import com.nubank.capitalgain.domain.model.Portfolio;
import com.nubank.capitalgain.domain.strategy.TaxCalculationStrategy;
import com.nubank.capitalgain.exception.InvalidOperationException;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.OperationType;
import com.nubank.capitalgain.generated.model.TaxResultDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.nubank.capitalgain.domain.constants.MessageConstants.ERROR_OPERATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxCalculatorServiceImplTest {

    private static final double TAX = 20.0;
    private static final int QUANTITY = 10;
    private static final double UNIT_COST = 5.0;

    @Mock
    private TaxCalculationStrategy strategy;

    private TaxCalculatorServiceImpl service;
    private OperationDTO operation;
    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        service = new TaxCalculatorServiceImpl(List.of(strategy));

        operation = new OperationDTO();
        operation.setOperation(OperationType.SELL);
        operation.setQuantity(QUANTITY);
        operation.setUnitCost(UNIT_COST);

        portfolio = new Portfolio();
    }

    @Test
    @DisplayName("Deve calcular imposto usando estratégia compatível")
    void shouldCalculateTaxUsingMatchingStrategy() {
        TaxResultDTO expectedResult = new TaxResultDTO();
        expectedResult.setTax(TAX);

        when(strategy.supports(operation, portfolio))
                .thenReturn(true);
        when(strategy.calculate(operation, portfolio))
                .thenReturn(expectedResult);

        TaxResultDTO result = service.calculate(operation, portfolio);

        assertEquals(expectedResult, result);
        verify(strategy).supports(operation, portfolio);
        verify(strategy).calculate(operation, portfolio);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhuma estratégia suporta a operação")
    void shouldThrowExceptionWhenNoStrategySupportsOperation() {
        when(strategy.supports(operation, portfolio))
                .thenReturn(false);

        final var exception = assertThrows(InvalidOperationException.class,
                () -> service.calculate(operation, portfolio));

        assertEquals(ERROR_OPERATION, exception.getMessage());
        verify(strategy).supports(operation, portfolio);
        verify(strategy, never()).calculate(any(), any());
    }
}
