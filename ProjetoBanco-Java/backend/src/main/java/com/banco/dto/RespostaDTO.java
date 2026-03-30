package com.banco.dto;

public class RespostaDTO {
    private boolean sucesso;
    private String mensagem;
    private Object dados;

    // Construtor com dados
    public RespostaDTO(boolean sucesso, String mensagem, Object dados) {
        this.sucesso   = sucesso;
        this.mensagem  = mensagem;
        this.dados     = dados;
    }

    // Construtor sem dados (apenas mensagem)
    public RespostaDTO(boolean sucesso, String mensagem) {
        this.sucesso   = sucesso;
        this.mensagem  = mensagem;
        this.dados     = null;
    }

    public boolean isSucesso()                { return sucesso; }
    public void setSucesso(boolean sucesso)   { this.sucesso = sucesso; }

    public String getMensagem()               { return mensagem; }
    public void setMensagem(String mensagem)  { this.mensagem = mensagem; }

    public Object getDados()                  { return dados; }
    public void setDados(Object dados)        { this.dados = dados; }
}
