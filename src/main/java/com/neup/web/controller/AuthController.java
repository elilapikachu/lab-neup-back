package com.neup.web.controller;

import com.neup.web.dto.AuthDTO.*;
import com.neup.web.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Autenticacion", description = "Operaciones de usuarios")
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Logguearse en el aplicativo")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Crear usuario")
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registro(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.registro(request);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Cambiar contraseña")
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

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/eliminar/{usuarioId}")
    public ResponseEntity<AuthResponse> eliminarCuenta(@PathVariable String usuarioId) {
        AuthResponse response = authService.eliminarCuenta(usuarioId);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @Operation(summary = "Crear usuario")
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "servicio", "auth"));
    }
}
