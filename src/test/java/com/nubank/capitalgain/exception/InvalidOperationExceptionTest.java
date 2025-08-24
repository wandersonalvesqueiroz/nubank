package com.nubank.capitalgain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvalidOperationExceptionTest {

    private static final String MESSAGE = "Operação não permitida";

    @Test
    @DisplayName("Deve lançar InvalidOperationException com mensagem personalizada")
    void shouldThrowInvalidOperationExceptionWithCustomMessage() {
        final var expectedMessage = MESSAGE;

        final var exception = assertThrows(InvalidOperationException.class,
                () -> { throw new InvalidOperationException(expectedMessage); });

        assertEquals(expectedMessage, exception.getMessage());
    }
}
