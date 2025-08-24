package com.nubank.capitalgain.controller.impl;

import com.nubank.capitalgain.domain.model.Portfolio;
import com.nubank.capitalgain.domain.service.TaxCalculatorService;
import com.nubank.capitalgain.exception.BaseExceptionHandler;
import com.nubank.capitalgain.exception.InvalidOperationException;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.OperationType;
import com.nubank.capitalgain.generated.model.TaxResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class CapitalGainsControllerImplTest {

    private static final int QUANTITY = 100;
    private static final double UNIT_COST = 10.0;
    private static final double TAX = 15.0;
    private static final String MESSAGE_ERROR = "Erro inesperado";

    @Mock
    private TaxCalculatorService calculator;

    @Mock
    private BaseExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CapitalGainsControllerImpl controller;

    private OperationDTO operation;
    private TaxResultDTO taxResult;

    @BeforeEach
    void setup() {
        operation = new OperationDTO();
        operation.setOperation(OperationType.SELL);
        operation.setQuantity(QUANTITY);
        operation.setUnitCost(UNIT_COST);

        taxResult = new TaxResultDTO();
        taxResult.setTax(TAX);
    }

    @Test
    @DisplayName("Deve calcular imposto corretamente para operações")
    void shouldCalculateTaxCorrectlyForGroupedOperations() {
        final var groupedOperations = List.of(List.of(operation));

        Mockito.when(calculator.calculate(eq(operation), any(Portfolio.class)))
                .thenReturn(taxResult);

        final var response = controller.calculateTax(groupedOperations, request);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(taxResult, response.getBody().getFirst().getFirst());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há operações")
    void shouldReturnEmptyListWhenNoOperationsProvided() {
        List<List<OperationDTO>> groupedOperations = List.of();

        final var response = controller.calculateTax(groupedOperations, request);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Deve tratar exceção de operação inválida e retornar erro estruturado")
    void shouldHandleInvalidOperationException() {
        final var groupedOperations = List.of(List.of(operation));
        final var exception = new InvalidOperationException(MESSAGE_ERROR);

        Mockito.when(calculator.calculate(eq(operation), any(Portfolio.class)))
                .thenThrow(exception);

        final var ex = assertThrows(InvalidOperationException.class,
                () -> controller.calculateTax(groupedOperations, request));

        assertEquals(MESSAGE_ERROR, ex.getMessage());
    }

    @Test
    @DisplayName("Deve tratar exceção genérica e retornar erro interno")
    void shouldHandleGenericException() {
        final var groupedOperations = List.of(List.of(operation));
        final var exception = new RuntimeException(MESSAGE_ERROR);

        Mockito.when(calculator.calculate(eq(operation), any(Portfolio.class)))
                .thenThrow(exception);

        final var ex = assertThrows(RuntimeException.class,
                () -> controller.calculateTax(groupedOperations, request));

        assertEquals(MESSAGE_ERROR, ex.getMessage());
    }
}
