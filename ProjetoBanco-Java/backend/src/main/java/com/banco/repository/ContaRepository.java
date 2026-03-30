package com.banco.repository;

import com.banco.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    // Busca conta pelo número (equivalente ao buscarConta do Banco.java original)
    Optional<Conta> findByNumero(int numero);

    // Endpoint exclusivo: top contas por saldo (GET /contas/top-saldos)
    @Query("SELECT c FROM Conta c ORDER BY c.saldo DESC")
    List<Conta> findAllOrderBySaldoDesc();

    // Filtro por tipo (CORRENTE / POUPANCA)
    List<Conta> findByTipo(String tipo);

    // Busca contas bloqueadas
    List<Conta> findByBloqueada(boolean bloqueada);

    // Verifica se número já existe (validação ao criar conta)
    boolean existsByNumero(int numero);
}
