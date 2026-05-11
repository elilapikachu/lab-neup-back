package com.neup.web.exeption;

import com.neup.web.dto.AuthDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthDTO.AuthResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("Error 500 en [{}]: {}", request.getRequestURI(), e.getMessage(), e);

        AuthDTO.AuthResponse response = new AuthDTO.AuthResponse(false,
                "Error interno del servidor: " + e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
