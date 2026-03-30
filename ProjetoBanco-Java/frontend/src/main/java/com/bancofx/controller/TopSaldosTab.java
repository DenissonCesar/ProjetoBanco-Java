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

public class TopSaldosTab {

    private final ApiService api;

    public TopSaldosTab(ApiService api) {
        this.api = api;
    }

    public Tab criarTab() {
        Tab tab = new Tab("Top Saldos");

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Ranking de Contas por Saldo");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label sub = new Label("Endpoint exclusivo: GET /contas/top-saldos");
        sub.setFont(Font.font("Arial", 12));
        sub.setTextFill(Color.web("#4f46e5"));

        Button btnCarregar = new Button("Carregar Ranking");
        btnCarregar.setStyle(MainApp.estiloBotaoPrimario());

        // Tabela de ranking
        VBox cardRanking = new VBox(10);
        cardRanking.setPadding(new Insets(20));
        cardRanking.setStyle(MainApp.estiloCard());
        cardRanking.setMaxWidth(560);

        Label lblRanking = new Label("Ranking");
        lblRanking.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextArea areaRanking = new TextArea();
        areaRanking.setEditable(false);
        areaRanking.setPrefHeight(300);
        areaRanking.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 13;");
        areaRanking.setText("Clique em 'Carregar Ranking' para ver.");

        cardRanking.getChildren().addAll(lblRanking, areaRanking);

        btnCarregar.setOnAction(e -> {
            String resp = api.topSaldos();
            areaRanking.setText(formatarRanking(resp));
        });

        root.getChildren().addAll(titulo, sub, btnCarregar, cardRanking);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f2f5;");
        tab.setContent(scroll);
        return tab;
    }

    private String formatarRanking(String json) {
        if (!json.contains("\"sucesso\":true")) return "Erro ao carregar o ranking.";

        String[] medalhas = {"🥇", "🥈", "🥉"};
        StringBuilder sb = new StringBuilder();
        String[] partes  = json.split("\\{");
        int pos = 0;

        for (String parte : partes) {
            if (!parte.contains("\"numero\"")) continue;

            String num     = extrairValor(parte, "numero");
            String tit     = extrairValor(parte, "titular");
            String saldo   = extrairValor(parte, "saldo");
            String tipo    = extrairValor(parte, "tipo");
            String bloq    = parte.contains("\"bloqueada\":true") ? " [BLOQUEADA]" : "";
            String medal   = pos < medalhas.length ? medalhas[pos] : String.format("%2d.", pos + 1);

            sb.append(String.format("%s  #%d%n", medal, pos + 1));
            sb.append(String.format("   Conta   : %s  (%s)%s%n", num, tipo, bloq));
            sb.append(String.format("   Titular : %s%n", tit));
            sb.append(String.format("   Saldo   : R$ %s%n", formatarNumero(saldo)));
            sb.append(String.format("   ─────────────────────────────%n"));
            pos++;
        }

        if (pos == 0) return "Nenhuma conta cadastrada.";
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

    private String formatarNumero(String num) {
        try {
            return String.format("%.2f", Double.parseDouble(num));
        } catch (Exception e) {
            return num;
        }
    }
}
