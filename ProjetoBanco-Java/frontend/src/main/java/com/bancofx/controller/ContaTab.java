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

public class ContaTab {

    private final ApiService api;

    public ContaTab(ApiService api) {
        this.api = api;
    }

    public Tab criarTab() {
        Tab tab = new Tab("Contas");

        SplitPane split = new SplitPane();
        split.setDividerPositions(0.42);
        split.setPadding(new Insets(16));

        // ── Painel Esquerdo: Criar Conta ───────────────────────────────────────
        VBox painelCriar = new VBox(14);
        painelCriar.setPadding(new Insets(24));
        painelCriar.setStyle(MainApp.estiloCard());

        Label tituloCriar = new Label("Nova Conta");
        tituloCriar.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label lblNumero = new Label("Número da conta");
        TextField numField = new TextField();
        numField.setPromptText("Ex: 101");
        numField.setStyle(estiloInputLocal());

        Label lblTitular = new Label("Titular");
        TextField titularField = new TextField();
        titularField.setPromptText("Nome completo");
        titularField.setStyle(estiloInputLocal());

        Label lblTipo = new Label("Tipo");
        ToggleGroup grupo = new ToggleGroup();
        RadioButton rbCorrente = new RadioButton("Corrente  (10% tributo)");
        RadioButton rbPoupanca = new RadioButton("Poupança  (isenta)");
        rbCorrente.setToggleGroup(grupo);
        rbPoupanca.setToggleGroup(grupo);
        rbCorrente.setSelected(true);
        HBox tipoBox = new HBox(16, rbCorrente, rbPoupanca);

        Label msgLabel = new Label();
        msgLabel.setWrapText(true);

        Button btnCriar = new Button("✚  Criar Conta");
        btnCriar.setMaxWidth(Double.MAX_VALUE);
        btnCriar.setStyle(MainApp.estiloBotaoPrimario());

        btnCriar.setOnAction(e -> {
            try {
                int numero   = Integer.parseInt(numField.getText().trim());
                String tit   = titularField.getText().trim();
                String tipo  = rbCorrente.isSelected() ? "CORRENTE" : "POUPANCA";

                if (tit.isEmpty()) { msgLabel.setText("⚠ Informe o titular."); return; }

                String resp = api.criarConta(numero, tit, tipo);
                if (resp.contains("\"sucesso\":true")) {
                    msgLabel.setTextFill(Color.web("#16a34a"));
                    msgLabel.setText("✔ Conta criada com sucesso!");
                    numField.clear(); titularField.clear();
                } else {
                    msgLabel.setTextFill(Color.web("#dc2626"));
                    msgLabel.setText("✘ " + extrairMensagem(resp));
                }
            } catch (NumberFormatException ex) {
                msgLabel.setTextFill(Color.web("#dc2626"));
                msgLabel.setText("⚠ Número inválido.");
            }
        });

        painelCriar.getChildren().addAll(
                tituloCriar,
                lblNumero, numField,
                lblTitular, titularField,
                lblTipo, tipoBox,
                btnCriar, msgLabel
        );

        // ── Painel Direito: Listar Contas ──────────────────────────────────────
        VBox painelListar = new VBox(12);
        painelListar.setPadding(new Insets(24));
        painelListar.setStyle(MainApp.estiloCard());

        Label tituloListar = new Label("Contas Cadastradas");
        tituloListar.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TextArea areaContas = new TextArea();
        areaContas.setEditable(false);
        areaContas.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12;");
        VBox.setVgrow(areaContas, Priority.ALWAYS);

        Button btnListar = new Button("Atualizar Lista");
        btnListar.setMaxWidth(Double.MAX_VALUE);
        btnListar.setStyle(MainApp.estiloBotaoPrimario());

        btnListar.setOnAction(e -> {
            String resp = api.listarContas();
            areaContas.setText(formatarListaContas(resp));
        });

        painelListar.getChildren().addAll(tituloListar, btnListar, areaContas);
        VBox.setVgrow(painelListar, Priority.ALWAYS);

        split.getItems().addAll(painelCriar, painelListar);
        tab.setContent(split);
        return tab;
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private String formatarListaContas(String json) {
        if (!json.contains("\"sucesso\":true")) return "Nenhuma conta encontrada.";

        StringBuilder sb = new StringBuilder();
        String[] partes = json.split("\\{");
        int i = 0;
        for (String parte : partes) {
            if (!parte.contains("\"numero\"")) continue;
            String num  = extrairValor(parte, "numero");
            String tit  = extrairValor(parte, "titular");
            String sal  = extrairValor(parte, "saldo");
            String tipo = extrairValor(parte, "tipo");
            String blq  = parte.contains("\"bloqueada\":true") ? " [BLOQUEADA]" : "";
            sb.append(String.format("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━%n"));
            sb.append(String.format("  Conta nº %-6s  %s%s%n", num, blq, ""));
            sb.append(String.format("  Titular : %s%n", tit));
            sb.append(String.format("  Tipo    : %s%n", tipo));
            sb.append(String.format("  Saldo   : R$ %s%n", sal));
        }
        if (sb.length() == 0) return "Nenhuma conta cadastrada ainda.";
        return sb.toString();
    }

    private String extrairMensagem(String json) {
        return extrairValor(json, "mensagem");
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

    private String estiloInputLocal() {
        return "-fx-background-color: #f8f9fa; " +
               "-fx-border-color: #dee2e6; " +
               "-fx-border-radius: 6; " +
               "-fx-background-radius: 6; " +
               "-fx-padding: 8 12 8 12; " +
               "-fx-font-size: 13;";
    }
}
