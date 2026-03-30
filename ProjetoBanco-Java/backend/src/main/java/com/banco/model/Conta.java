package com.banco.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private int numero;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private double saldo = 0.0;

    // ── BLOQUEIO DE CONTA (funcionalidade adicional) ──────────────────────────
    @Column(nullable = false)
    private boolean bloqueada = false;

    // Tipo discriminador exposto como campo legível
    @Column(name = "tipo", insertable = false, updatable = false)
    private String tipo;

    public Conta() {}

    public Conta(String titular, int numero) {
        this.titular   = titular;
        this.numero    = numero;
        this.saldo     = 0.0;
        this.bloqueada = false;
    }

    // ── Operações bancárias ───────────────────────────────────────────────────

    public void depositar(double valor) {
        if (bloqueada) throw new IllegalStateException("Conta bloqueada. Operação não permitida.");
        if (valor <= 0) throw new IllegalArgumentException("Valor de depósito deve ser positivo.");
        this.saldo += valor;
    }

    public void sacar(double valor) {
        if (bloqueada) throw new IllegalStateException("Conta bloqueada. Operação não permitida.");
        if (valor <= 0) throw new IllegalArgumentException("Valor de saque deve ser positivo.");
        if (valor > this.saldo) throw new IllegalStateException("Saldo insuficiente.");
        this.saldo -= valor;
    }

    public void transferir(Conta destino, double valor) {
        if (this.bloqueada)        throw new IllegalStateException("Conta de origem bloqueada. Operação não permitida.");
        if (destino.isBloqueada()) throw new IllegalStateException("Conta de destino bloqueada. Operação não permitida.");
        this.sacar(valor);
        destino.depositar(valor);
    }

    // ── Tributo (polimorfismo mantido da atividade anterior) ─────────────────
    public abstract double calcularTributo();

    // ── Getters e Setters ─────────────────────────────────────────────────────

    public Long getId()                        { return id; }
    public void setId(Long id)                 { this.id = id; }

    public int getNumero()                     { return numero; }
    public void setNumero(int numero)          { this.numero = numero; }

    public String getTitular()                 { return titular; }
    public void setTitular(String titular)     { this.titular = titular; }

    public double getSaldo()                   { return saldo; }
    public void setSaldo(double saldo)         { this.saldo = saldo; }

    public boolean isBloqueada()               { return bloqueada; }
    public void setBloqueada(boolean bloqueada){ this.bloqueada = bloqueada; }

    public String getTipo()                    { return tipo; }
    public void setTipo(String tipo)           { this.tipo = tipo; }
}
