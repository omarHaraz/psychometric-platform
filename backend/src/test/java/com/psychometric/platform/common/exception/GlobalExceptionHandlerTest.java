package com.psychometric.platform.common.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/api/reports/test-token/pdf");
        response = new MockHttpServletResponse();
    }

    @Test
    void testHandleAsyncRequestNotUsable_DoesNotThrow() {
        AsyncRequestNotUsableException ex = new AsyncRequestNotUsableException("Async request failed");
        assertDoesNotThrow(() -> exceptionHandler.handleAsyncRequestNotUsable(ex, request));
    }

    @Test
    void testHandleAllExceptions_ClientAbortIOException_ReturnsNull() {
        IOException cause = new IOException("An established connection was aborted by the software in your host machine");
        ResponseEntity<ErrorResponse> result = exceptionHandler.handleAllExceptions(cause, request, response);

        assertNull(result, "Client abort should return null so no body is written to a closed socket");
    }

    @Test
    void testHandleAllExceptions_BrokenPipe_ReturnsNull() {
        IOException cause = new IOException("Broken pipe");
        ResponseEntity<ErrorResponse> result = exceptionHandler.handleAllExceptions(cause, request, response);
 
        assertNull(result, "Broken pipe should return null");
    }

    @Test
    void testHandleAllExceptions_ResponseAlreadyCommitted_ReturnsNull() {
        response.setCommitted(true);
        RuntimeException ex = new RuntimeException("Something failed after commit");
        ResponseEntity<ErrorResponse> result = exceptionHandler.handleAllExceptions(ex, request, response);

        assertNull(result, "Committed response should return null to prevent stream corruption");
    }

    @Test
    void testHandleAllExceptions_NormalException_ReturnsJson500() {
        RuntimeException ex = new RuntimeException("Unexpected error");
        ResponseEntity<ErrorResponse> result = exceptionHandler.handleAllExceptions(ex, request, response);

        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, result.getHeaders().getContentType());
        assertNotNull(result.getBody());
        assertEquals("Unexpected error", result.getBody().getMessage());
        assertEquals("/api/reports/test-token/pdf", result.getBody().getPath());
    }
}
