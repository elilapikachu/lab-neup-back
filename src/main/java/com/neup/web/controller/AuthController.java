package com.neup.web.controller;

import com.neup.web.dto.AuthDTO.*;
import com.neup.web.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Puerto default de Angular para poder ejecutar aplicativo
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.registro(request);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<AuthResponse> cambiarPassword(@RequestBody Map<String, String> body) {
        String usuarioId     = body.get("usuarioId");
        String passwordActual = body.get("passwordActual");
        String nuevaPassword  = body.get("nuevaPassword");

        if (usuarioId == null || passwordActual == null || nuevaPassword == null) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "Faltan campos obligatorios"));
        }

        AuthResponse response = authService.cambiarPassword(usuarioId, passwordActual, nuevaPassword);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping("/eliminar/{usuarioId}")
    public ResponseEntity<AuthResponse> eliminarCuenta(@PathVariable String usuarioId) {
        AuthResponse response = authService.eliminarCuenta(usuarioId);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "servicio", "auth"));
    }
}
