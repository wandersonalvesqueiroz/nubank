package com.nubank.capitalgain.controller;


import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.TaxResultDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CapitalGainsController {
    ResponseEntity<List<List<TaxResultDTO>>> calculateTax(List<List<OperationDTO>> operations, HttpServletRequest request);
}
