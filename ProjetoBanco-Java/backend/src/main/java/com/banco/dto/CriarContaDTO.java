package com.banco.dto;

public class CriarContaDTO {
    private int numero;
    private String titular;
    private String tipo;

    public int getNumero()               { return numero; }
    public void setNumero(int numero)    { this.numero = numero; }

    public String getTitular()                { return titular; }
    public void setTitular(String titular)    { this.titular = titular; }

    public String getTipo()              { return tipo; }
    public void setTipo(String tipo)     { this.tipo = tipo; }
}
