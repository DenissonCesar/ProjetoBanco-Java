package com.banco.controller;

import com.banco.dto.*;
import com.banco.model.Conta;
import com.banco.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
@CrossOrigin(origins = "*") // permite requisições do frontend JavaFX/Web
public class ContaController {

    @Autowired
    private ContaService contaService;

    // ── POST /contas ──────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<RespostaDTO> criarConta(@RequestBody CriarContaDTO dto) {
        try {
            Conta conta = contaService.criarConta(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RespostaDTO(true, "Conta criada com sucesso.", conta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new RespostaDTO(false, e.getMessage()));
        }
    }

    // ── GET /contas ───────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<RespostaDTO> listarContas() {
        List<Conta> contas = contaService.listarContas();
        return ResponseEntity.ok(new RespostaDTO(true, "Contas listadas com sucesso.", contas));
    }

    // ── GET /contas/{numero} ──────────────────────────────────────────────────
    @GetMapping("/{numero}")
    public ResponseEntity<RespostaDTO> buscarConta(@PathVariable int numero) {
        try {
            Conta conta = contaService.buscarPorNumero(numero);
            return ResponseEntity.ok(new RespostaDTO(true, "Conta encontrada.", conta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RespostaDTO(false, e.getMessage()));
        }
    }

    // ── POST /contas/depositar ────────────────────────────────────────────────
    @PostMapping("/depositar")
    public ResponseEntity<RespostaDTO> depositar(@RequestBody OperacaoDTO dto) {
        try {
            Conta conta = contaService.depositar(dto);
            return ResponseEntity.ok(new RespostaDTO(true, "Depósito realizado com sucesso.", conta));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RespostaDTO(false, e.getMessage()));
        }
    }

    // ── POST /contas/sacar ────────────────────────────────────────────────────
    @PostMapping("/sacar")
    public ResponseEntity<RespostaDTO> sacar(@RequestBody OperacaoDTO dto) {
        try {
            Conta conta = contaService.sacar(dto);
            return ResponseEntity.ok(new RespostaDTO(true, "Saque realizado com sucesso.", conta));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RespostaDTO(false, e.getMessage()));
        }
    }

    // ── POST /contas/transferir ───────────────────────────────────────────────
    @PostMapping("/transferir")
    public ResponseEntity<RespostaDTO> transferir(@RequestBody TransferenciaDTO dto) {
        try {
            contaService.transferir(dto);
            return ResponseEntity.ok(new RespostaDTO(true, "Transferência realizada com sucesso."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new RespostaDTO(false, e.getMessage()));
        }
    }

    // ── GET /contas/tributos ──────────────────────────────────────────────────
    @GetMapping("/tributos")
    public ResponseEntity<RespostaDTO> calcularTributos() {
        return ResponseEntity.ok(
                new RespostaDTO(true, "Tributos calculados.", contaService.calcularTributos())
        );
    }

    // ── POST /contas/bloquear (funcionalidade adicional) ──────────────────────
    @PostMapping("/bloquear")
    public ResponseEntity<RespostaDTO> alterarBloqueio(@RequestBody BloqueioDTO dto) {
        try {
            Conta conta = contaService.alterarBloqueio(dto);
            String msg = dto.isBloquear() ? "Conta bloqueada." : "Conta desbloqueada.";
            return ResponseEntity.ok(new RespostaDTO(true, msg, conta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RespostaDTO(false, e.getMessage()));
        }
    }

    // ── GET /contas/top-saldos (endpoint exclusivo) ───────────────────────────
    @GetMapping("/top-saldos")
    public ResponseEntity<RespostaDTO> topSaldos() {
        List<Conta> contas = contaService.topSaldos();
        return ResponseEntity.ok(new RespostaDTO(true, "Top contas por saldo.", contas));
    }
}
