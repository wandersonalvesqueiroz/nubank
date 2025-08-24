package com.nubank.capitalgain.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseExceptionHandlerTest {

    private static final String MESSAGE_ERROR_400 = "Campo inválido";
    private static final String MESSAGE_ERROR_500 = "Erro inesperado";
    private static final String PATH = "/api/test";
    private static final String ERROR_400 = "Argumento inválido";
    private static final String ERROR_500 = "Erro interno";

    private BaseExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new BaseExceptionHandler();
        when(request.getRequestURI())
                .thenReturn(PATH);
    }

    @Test
    @DisplayName("Deve retornar BAD_REQUEST para IllegalArgumentException")
    void shouldReturnBadRequestForIllegalArgumentException() {
        final var ex = new IllegalArgumentException(MESSAGE_ERROR_400);
        final var response = handler.handleException(ex, request);
        final var body = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.getStatus());
        assertEquals(ERROR_400, body.getError());
        assertEquals(MESSAGE_ERROR_400, body.getMessage());
        assertEquals(PATH, body.getPath());
    }

    @Test
    @DisplayName("Deve retornar INTERNAL_SERVER_ERROR para exceções genéricas")
    void shouldReturnInternalServerErrorForGenericException() {
        final var ex = new RuntimeException(MESSAGE_ERROR_500);
        final var response = handler.handleException(ex, request);
        final var body = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.getStatus());
        assertEquals(ERROR_500, body.getError());
        assertEquals(MESSAGE_ERROR_500, body.getMessage());
        assertEquals(PATH, body.getPath());
    }
}
