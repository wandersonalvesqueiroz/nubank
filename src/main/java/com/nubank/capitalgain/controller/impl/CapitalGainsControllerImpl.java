package com.nubank.capitalgain.controller.impl;

import com.nubank.capitalgain.controller.CapitalGainsController;
import com.nubank.capitalgain.domain.model.Portfolio;
import com.nubank.capitalgain.domain.service.TaxCalculatorService;
import com.nubank.capitalgain.exception.BaseExceptionHandler;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.TaxResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/capital-gains")
@RequiredArgsConstructor
public class CapitalGainsControllerImpl implements CapitalGainsController {

    private final TaxCalculatorService calculator;
    private final BaseExceptionHandler exceptionHandler;

    @PostMapping
    public ResponseEntity<List<List<TaxResultDTO>>> calculateTax(@RequestBody List<List<OperationDTO>> groupedOperations, HttpServletRequest request) {
        List<List<TaxResultDTO>> allResults = new ArrayList<>();

        for (List<OperationDTO> operations : groupedOperations) {
            Portfolio portfolio = new Portfolio();
            List<TaxResultDTO> results = operations.stream()
                    .map(op -> calculator.calculate(op, portfolio))
                    .toList();
            allResults.add(results);
        }

        return ResponseEntity.ok(allResults);
    }
}