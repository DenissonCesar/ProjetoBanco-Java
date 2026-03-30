package com.banco.dto;

public class BloqueioDTO {
    private int numero;
    private boolean bloquear;

    public int getNumero()              { return numero; }
    public void setNumero(int numero)   { this.numero = numero; }

    public boolean isBloquear()               { return bloquear; }
    public void setBloquear(boolean bloquear) { this.bloquear = bloquear; }
}
