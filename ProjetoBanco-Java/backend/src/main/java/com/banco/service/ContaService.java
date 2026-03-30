package com.banco.service;

import com.banco.dto.*;
import com.banco.model.Conta;
import com.banco.model.ContaCorrente;
import com.banco.model.ContaPoupanca;
import com.banco.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    // ── Criar Conta ───────────────────────────────────────────────────────────
    @Transactional
    public Conta criarConta(CriarContaDTO dto) {
        if (contaRepository.existsByNumero(dto.getNumero())) {
            throw new IllegalArgumentException("Já existe uma conta com o número " + dto.getNumero());
        }

        Conta conta;
        String tipo = dto.getTipo().toUpperCase();

        if (tipo.equals("CORRENTE")) {
            conta = new ContaCorrente(dto.getTitular(), dto.getNumero());
        } else if (tipo.equals("POUPANCA")) {
            conta = new ContaPoupanca(dto.getTitular(), dto.getNumero());
        } else {
            throw new IllegalArgumentException("Tipo inválido. Use CORRENTE ou POUPANCA.");
        }

        return contaRepository.save(conta);
    }

    // ── Listar Contas ─────────────────────────────────────────────────────────
    public List<Conta> listarContas() {
        return contaRepository.findAll();
    }

    // ── Buscar por Número ─────────────────────────────────────────────────────
    public Conta buscarPorNumero(int numero) {
        return contaRepository.findByNumero(numero)
                .orElseThrow(() -> new IllegalArgumentException("Conta " + numero + " não encontrada."));
    }

    // ── Depositar ─────────────────────────────────────────────────────────────
    @Transactional
    public Conta depositar(OperacaoDTO dto) {
        Conta conta = buscarPorNumero(dto.getNumero());
        conta.depositar(dto.getValor());
        return contaRepository.save(conta);
    }

    // ── Sacar ─────────────────────────────────────────────────────────────────
    @Transactional
    public Conta sacar(OperacaoDTO dto) {
        Conta conta = buscarPorNumero(dto.getNumero());
        conta.sacar(dto.getValor());
        return contaRepository.save(conta);
    }

    // ── Transferir ────────────────────────────────────────────────────────────
    @Transactional
    public void transferir(TransferenciaDTO dto) {
        Conta origem  = buscarPorNumero(dto.getOrigem());
        Conta destino = buscarPorNumero(dto.getDestino());
        origem.transferir(destino, dto.getValor());
        contaRepository.save(origem);
        contaRepository.save(destino);
    }

    // ── Calcular Tributos ─────────────────────────────────────────────────────
    public Map<String, Object> calcularTributos() {
        List<Conta> contas = contaRepository.findAll();
        double total = 0;
        Map<Integer, Double> detalhe = new HashMap<>();

        for (Conta c : contas) {
            double tributo = c.calcularTributo();
            if (tributo > 0) {
                detalhe.put(c.getNumero(), tributo);
                total += tributo;
            }
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("totalTributos", total);
        resultado.put("detalhePorConta", detalhe);
        return resultado;
    }

    // ── Bloquear / Desbloquear Conta (funcionalidade adicional) ───────────────
    @Transactional
    public Conta alterarBloqueio(BloqueioDTO dto) {
        Conta conta = buscarPorNumero(dto.getNumero());
        conta.setBloqueada(dto.isBloquear());
        return contaRepository.save(conta);
    }

    // ── Top Saldos (endpoint exclusivo) ───────────────────────────────────────
    public List<Conta> topSaldos() {
        return contaRepository.findAllOrderBySaldoDesc();
    }
}
