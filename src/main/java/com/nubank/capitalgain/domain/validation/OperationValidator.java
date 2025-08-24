package com.nubank.capitalgain.domain.validation;

import com.nubank.capitalgain.exception.InvalidOperationException;
import com.nubank.capitalgain.generated.model.OperationDTO;
import com.nubank.capitalgain.generated.model.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.nubank.capitalgain.domain.constants.MessageConstants.*;

@Component
@RequiredArgsConstructor
public class OperationValidator {

    public boolean isBuy(OperationType operationType) {
        return OperationType.BUY.equals(operationType);
    }

    public boolean isSell(OperationType operationType) {
        return OperationType.SELL.equals(operationType);
    }

    public static void validateOperation(OperationDTO operation) {
        validateQuantity(operation);
        validateUnitCost(operation);
        validateOperationType(operation);
    }

    private static void validateQuantity(OperationDTO operation) {
        if (operation.getQuantity() <= 0) {
            throw new InvalidOperationException(ERROR_QUANTITY);
        }
    }

    private static void validateUnitCost(OperationDTO operation) {
        if (operation.getUnitCost() <= 0) {
            throw new InvalidOperationException(ERROR_UNIT_COST);
        }
    }

    private static void validateOperationType(OperationDTO operation) {
        try {
            OperationType.valueOf(operation.getOperation().getValue().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new InvalidOperationException(String.format(ERROR_OPERATION_TYPE, operation.getOperation()));
        }
    }
}
