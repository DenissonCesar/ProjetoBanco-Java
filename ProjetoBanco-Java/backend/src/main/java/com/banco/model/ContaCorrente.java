package com.banco.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CORRENTE")
public class ContaCorrente extends Conta {

    public ContaCorrente() {}

    public ContaCorrente(String titular, int numero) {
        super(titular, numero);
    }

    /**
     * ContaCorrente paga 10% de tributo sobre o saldo.
     * Mantida a lógica da atividade anterior.
     */
    @Override
    public double calcularTributo() {
        return getSaldo() * 0.10;
    }
}
