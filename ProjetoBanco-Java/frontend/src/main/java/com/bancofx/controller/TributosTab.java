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

public class TributosTab {

    private final ApiService api;

    public TributosTab(ApiService api) {
        this.api = api;
    }

    public Tab criarTab() {
        Tab tab = new Tab("Tributos");

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Cálculo de Tributos");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label sub = new Label("Contas Correntes: 10% sobre o saldo  |  Contas Poupança: isenta");
        sub.setFont(Font.font("Arial", 13));
        sub.setTextFill(Color.web("#666666"));

        // Card do total
        VBox cardTotal = new VBox(8);
        cardTotal.setPadding(new Insets(24));
        cardTotal.setStyle(MainApp.estiloCard());
        cardTotal.setAlignment(Pos.CENTER);
        cardTotal.setMaxWidth(400);

        Label lblTotalTitulo = new Label("TOTAL DE TRIBUTOS");
        lblTotalTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        lblTotalTitulo.setTextFill(Color.web("#888888"));

        Label lblTotalValor = new Label("—");
        lblTotalValor.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        lblTotalValor.setTextFill(Color.web("#4f46e5"));

        cardTotal.getChildren().addAll(lblTotalTitulo, lblTotalValor);

        // Detalhe por conta
        VBox cardDetalhe = new VBox(10);
        cardDetalhe.setPadding(new Insets(20));
        cardDetalhe.setStyle(MainApp.estiloCard());
        cardDetalhe.setMaxWidth(500);

        Label lblDetalhe = new Label("Detalhamento por Conta");
        lblDetalhe.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextArea areaDetalhe = new TextArea();
        areaDetalhe.setEditable(false);
        areaDetalhe.setPrefHeight(220);
        areaDetalhe.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12;");

        cardDetalhe.getChildren().addAll(lblDetalhe, areaDetalhe);

        Button btnCalcular = new Button("Calcular Tributos");
        btnCalcular.setStyle(MainApp.estiloBotaoPrimario());

        btnCalcular.setOnAction(e -> {
            String resp = api.calcularTributos();
            if (resp.contains("\"sucesso\":true")) {
                String total   = extrairValor(resp, "totalTributos");
                lblTotalValor.setText("R$ " + formatarNumero(total));
                areaDetalhe.setText(formatarDetalhe(resp));
            } else {
                lblTotalValor.setText("Erro");
                areaDetalhe.setText("Não foi possível calcular os tributos.\n" + resp);
            }
        });

        root.getChildren().addAll(titulo, sub, btnCalcular, cardTotal, cardDetalhe);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f2f5;");
        tab.setContent(scroll);
        return tab;
    }

    private String formatarDetalhe(String json) {
        // Extrai o bloco detalhePorConta e formata linha a linha
        int inicio = json.indexOf("\"detalhePorConta\":");
        if (inicio == -1) return "Nenhum tributo a cobrar.";

        int abre  = json.indexOf('{', inicio);
        int fecha = json.indexOf('}', abre);
        if (abre == -1 || fecha == -1) return "Nenhuma conta corrente com saldo.";

        String bloco = json.substring(abre + 1, fecha).trim();
        if (bloco.isEmpty()) return "Nenhuma conta corrente com saldo tributável.";

        StringBuilder sb = new StringBuilder();
        String[] pares = bloco.split(",");
        for (String par : pares) {
            String[] kv = par.split(":");
            if (kv.length == 2) {
                String conta  = kv[0].replaceAll("[\"{}]", "").trim();
                String tributo = kv[1].replaceAll("[\"{}]", "").trim();
                sb.append(String.format("  Conta %-8s → R$ %s%n", conta, formatarNumero(tributo)));
            }
        }
        return sb.length() > 0 ? sb.toString() : "Nenhum tributo a cobrar.";
    }

    private String extrairValor(String json, String chave) {
        String busca = "\"" + chave + "\":";
        int idx = json.indexOf(busca);
        if (idx == -1) return "0";
        int inicio = idx + busca.length();
        int fim = json.indexOf(',', inicio);
        if (fim == -1) fim = json.indexOf('}', inicio);
        return json.substring(inicio, fim).trim();
    }

    private String formatarNumero(String num) {
        try {
            double val = Double.parseDouble(num);
            return String.format("%.2f", val);
        } catch (Exception e) {
            return num;
        }
    }
}
