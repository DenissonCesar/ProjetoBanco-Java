package com.banco.service;

import com.banco.dto.LoginDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Credenciais lidas do application.properties (mantém a lógica do Gerente/Funcionario original)
    @Value("${banco.gerente.nome}")
    private String nomeGerente;

    @Value("${banco.gerente.senha}")
    private String senhaGerente;

    /**
     * Autentica o gerente verificando nome e senha.
     * Equivalente ao Gerente.autenticar() da atividade anterior.
     */
    public boolean autenticar(LoginDTO dto) {
        return nomeGerente.equals(dto.getNome()) && senhaGerente.equals(dto.getSenha());
    }
}
