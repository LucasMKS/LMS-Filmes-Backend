package com.lucasm.lmsfilmes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lucasm.lmsfilmes.dto.AuthDTO;
import com.lucasm.lmsfilmes.service.AuthService;

/**
 * Controlador de autenticação.
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Método para registrar um novo usuário.
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDTO reg) {
        return ResponseEntity.ok(authService.register(reg));
    }

    // Método para realizar login.
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO req) {
        return ResponseEntity.ok(authService.login(req));
    }
}