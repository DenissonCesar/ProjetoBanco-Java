package com.banco.controller;

import com.banco.dto.LoginDTO;
import com.banco.dto.RespostaDTO;
import com.banco.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ── POST /auth/login ──────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<RespostaDTO> login(@RequestBody LoginDTO dto) {
        boolean autenticado = authService.autenticar(dto);

        if (autenticado) {
            return ResponseEntity.ok(new RespostaDTO(true, "Autenticação bem-sucedida. Acesso permitido."));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new RespostaDTO(false, "Credenciais inválidas. Acesso negado."));
        }
    }
}
