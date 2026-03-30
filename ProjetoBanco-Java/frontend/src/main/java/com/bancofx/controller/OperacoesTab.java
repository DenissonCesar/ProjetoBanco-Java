package com.bancofx.controller;

import com.bancofx.MainApp;
import com.bancofx.service.ApiService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OperacoesTab {

    private final ApiService api;

    public OperacoesTab(ApiService api) {
        this.api = api;
    }

    public Tab criarTab() {
        Tab tab = new Tab("Operações");

        VBox root = new VBox(16);
        root.setPadding(new Insets(20));

        // ── Área de resultado compartilhada ────────────────────────────────────
        TextArea resultado = new TextArea();
        resultado.setEditable(false);
        resultado.setPrefHeight(130);
        resultado.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12;");

        // ── Cards das operações ────────────────────────────────────────────────
        HBox linha1 = new HBox(16);
        linha1.getChildren().addAll(
                cardDeposito(resultado),
                cardSaque(resultado)
        );
        HBox.setHgrow(linha1.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(linha1.getChildren().get(1), Priority.ALWAYS);

        VBox cardTransf = cardTransferencia(resultado);

        Label lblResultado = new Label("Resultado da Operação");
        lblResultado.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        root.getChildren().addAll(linha1, cardTransf, lblResultado, resultado);
        VBox.setVgrow(resultado, Priority.ALWAYS);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f2f5;");
        tab.setContent(scroll);
        return tab;
    }

    // ── Card Depósito ──────────────────────────────────────────────────────────
    private VBox cardDeposito(TextArea resultado) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle(MainApp.estiloCard());
        card.setPrefWidth(300);

        Label titulo = new Label("⬆ Depósito");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        TextField numField = new TextField();
        numField.setPromptText("Número da conta");
        numField.setStyle(estiloInput());

        TextField valorField = new TextField();
        valorField.setPromptText("Valor (ex: 500.00)");
        valorField.setStyle(estiloInput());

        Button btn = new Button("Depositar");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(MainApp.estiloBotaoSucesso());

        btn.setOnAction(e -> {
            try {
                int num    = Integer.parseInt(numField.getText().trim());
                double val = Double.parseDouble(valorField.getText().trim().replace(",", "."));
                String resp = api.depositar(num, val);
                resultado.setText(formatarResposta(resp, "Depósito"));
                numField.clear(); valorField.clear();
            } catch (NumberFormatException ex) {
                resultado.setText("Valores inválidos. Use ponto para decimais (ex: 150.00)");
            }
        });

        card.getChildren().addAll(titulo, new Label("Conta:"), numField,
                new Label("Valor:"), valorField, btn);
        return card;
    }

    // ── Card Saque ─────────────────────────────────────────────────────────────
    private VBox cardSaque(TextArea resultado) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle(MainApp.estiloCard());
        card.setPrefWidth(300);

        Label titulo = new Label("⬇ Saque");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        TextField numField = new TextField();
        numField.setPromptText("Número da conta");
        numField.setStyle(estiloInput());

        TextField valorField = new TextField();
        valorField.setPromptText("Valor (ex: 200.00)");
        valorField.setStyle(estiloInput());

        Button btn = new Button("Sacar");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(MainApp.estiloBotaoPerigo());

        btn.setOnAction(e -> {
            try {
                int num    = Integer.parseInt(numField.getText().trim());
                double val = Double.parseDouble(valorField.getText().trim().replace(",", "."));
                String resp = api.sacar(num, val);
                resultado.setText(formatarResposta(resp, "Saque"));
                numField.clear(); valorField.clear();
            } catch (NumberFormatException ex) {
                resultado.setText("⚠ Valores inválidos.");
            }
        });

        card.getChildren().addAll(titulo, new Label("Conta:"), numField,
                new Label("Valor:"), valorField, btn);
        return card;
    }

    // ── Card Transferência ─────────────────────────────────────────────────────
    private VBox cardTransferencia(TextArea resultado) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle(MainApp.estiloCard());

        Label titulo = new Label("↔  Transferência");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        HBox campos = new HBox(12);

        VBox colOrigem = new VBox(6);
        TextField origemField = new TextField();
        origemField.setPromptText("Conta origem");
        origemField.setStyle(estiloInput());
        colOrigem.getChildren().addAll(new Label("Conta Origem:"), origemField);

        VBox colDestino = new VBox(6);
        TextField destinoField = new TextField();
        destinoField.setPromptText("Conta destino");
        destinoField.setStyle(estiloInput());
        colDestino.getChildren().addAll(new Label("Conta Destino:"), destinoField);

        VBox colValor = new VBox(6);
        TextField valorField = new TextField();
        valorField.setPromptText("Valor");
        valorField.setStyle(estiloInput());
        colValor.getChildren().addAll(new Label("Valor:"), valorField);

        HBox.setHgrow(colOrigem,  Priority.ALWAYS);
        HBox.setHgrow(colDestino, Priority.ALWAYS);
        HBox.setHgrow(colValor,   Priority.ALWAYS);
        campos.getChildren().addAll(colOrigem, colDestino, colValor);

        Button btn = new Button("Transferir");
        btn.setStyle(MainApp.estiloBotaoPrimario());

        btn.setOnAction(e -> {
            try {
                int ori    = Integer.parseInt(origemField.getText().trim());
                int des    = Integer.parseInt(destinoField.getText().trim());
                double val = Double.parseDouble(valorField.getText().trim().replace(",", "."));
                String resp = api.transferir(ori, des, val);
                resultado.setText(formatarResposta(resp, "Transferência"));
                origemField.clear(); destinoField.clear(); valorField.clear();
            } catch (NumberFormatException ex) {
                resultado.setText("⚠ Valores inválidos.");
            }
        });

        card.getChildren().addAll(titulo, campos, btn);
        return card;
    }

    // ── Helpers ────────────────────────────────────────────────────────────────
    private String formatarResposta(String json, String operacao) {
        boolean ok = json.contains("\"sucesso\":true");
        String msg = extrairValor(json, "mensagem");
        String saldo = extrairValor(json, "saldo");

        StringBuilder sb = new StringBuilder();
        sb.append(ok ? "✔ " : "✘ ").append(operacao).append("\n");
        sb.append("Mensagem : ").append(msg).append("\n");
        if (!saldo.equals("–")) sb.append("Novo saldo: R$ ").append(saldo).append("\n");
        return sb.toString();
    }

    private String extrairValor(String json, String chave) {
        String busca = "\"" + chave + "\":";
        int idx = json.indexOf(busca);
        if (idx == -1) return "–";
        int inicio = idx + busca.length();
        if (json.charAt(inicio) == '"') {
            int fim = json.indexOf('"', inicio + 1);
            return json.substring(inicio + 1, fim);
        } else {
            int fim = json.indexOf(',', inicio);
            if (fim == -1) fim = json.indexOf('}', inicio);
            return json.substring(inicio, fim).trim();
        }
    }

    private String estiloInput() {
        return "-fx-background-color: #f8f9fa; " +
               "-fx-border-color: #dee2e6; " +
               "-fx-border-radius: 6; " +
               "-fx-background-radius: 6; " +
               "-fx-padding: 8 12 8 12; " +
               "-fx-font-size: 13;";
    }
}
