package com.banco.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("POUPANCA")
public class ContaPoupanca extends Conta {

    public ContaPoupanca() {}

    public ContaPoupanca(String titular, int numero) {
        super(titular, numero);
    }

    /**
     * ContaPoupanca é isenta de tributo.
     */
    @Override
    public double calcularTributo() {
        return 0.0;
    }
}
