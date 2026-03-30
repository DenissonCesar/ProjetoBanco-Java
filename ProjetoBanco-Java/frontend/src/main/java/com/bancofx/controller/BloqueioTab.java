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

public class BloqueioTab {

    private final ApiService api;

    public BloqueioTab(ApiService api) {
        this.api = api;
    }

    public Tab criarTab() {
        Tab tab = new Tab("Bloqueio");

        VBox root = new VBox(24);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Bloqueio / Desbloqueio de Conta");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label sub = new Label("Contas bloqueadas não permitem depósito, saque ou transferência.");
        sub.setFont(Font.font("Arial", 13));
        sub.setTextFill(Color.web("#666666"));
        sub.setWrapText(true);

        // ── Card de controle ───────────────────────────────────────────────────
        VBox card = new VBox(16);
        card.setPadding(new Insets(28));
        card.setStyle(MainApp.estiloCard());
        card.setMaxWidth(480);
        card.setAlignment(Pos.TOP_LEFT);

        Label lblNum = new Label("Número da conta:");
        lblNum.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        TextField numField = new TextField();
        numField.setPromptText("Ex: 101");
        numField.setStyle(estiloInput());
        numField.setMaxWidth(200);

        // Botão consultar status
        Button btnConsultar = new Button("Consultar Status");
        btnConsultar.setStyle(MainApp.estiloBotaoPrimario());

        Label lblStatus = new Label();
        lblStatus.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lblStatus.setWrapText(true);

        btnConsultar.setOnAction(e -> {
            try {
                int num = Integer.parseInt(numField.getText().trim());
                String resp = api.buscarConta(num);
                if (resp.contains("\"sucesso\":true")) {
                    boolean bloqueada = resp.contains("\"bloqueada\":true");
                    String titular    = extrairValor(resp, "titular");
                    String saldo      = extrairValor(resp, "saldo");
                    String status     = bloqueada ? "BLOQUEADA" : "ATIVA";
                    lblStatus.setTextFill(bloqueada ? Color.web("#dc2626") : Color.web("#16a34a"));
                    lblStatus.setText(
                        "Titular : " + titular + "\n" +
                        "Saldo   : R$ " + saldo + "\n" +
                        "Status  : " + status
                    );
                } else {
                    lblStatus.setTextFill(Color.web("#dc2626"));
                    lblStatus.setText("Conta não encontrada.");
                }
            } catch (NumberFormatException ex) {
                lblStatus.setTextFill(Color.web("#dc2626"));
                lblStatus.setText("⚠ Número inválido.");
            }
        });

        // Separador visual
        Separator sep = new Separator();

        Label lblAcao = new Label("Ação:");
        lblAcao.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        HBox botoesAcao = new HBox(14);

        Button btnBloquear = new Button("Bloquear Conta");
        btnBloquear.setStyle(MainApp.estiloBotaoPerigo());

        Button btnDesbloquear = new Button("Desbloquear Conta");
        btnDesbloquear.setStyle(MainApp.estiloBotaoSucesso());

        Label msgLabel = new Label();
        msgLabel.setFont(Font.font("Arial", 13));
        msgLabel.setWrapText(true);

        btnBloquear.setOnAction(e -> executarBloqueio(numField, true, lblStatus, msgLabel));
        btnDesbloquear.setOnAction(e -> executarBloqueio(numField, false, lblStatus, msgLabel));

        botoesAcao.getChildren().addAll(btnBloquear, btnDesbloquear);

        // Aviso
        Label aviso = new Label(
            "⚠  Atenção: apenas o gerente autenticado pode bloquear ou desbloquear contas. " +
            "O bloqueio é imediato e permanece até ser revertido manualmente."
        );
        aviso.setWrapText(true);
        aviso.setFont(Font.font("Arial", 11));
        aviso.setTextFill(Color.web("#888888"));
        aviso.setMaxWidth(420);

        card.getChildren().addAll(
                lblNum, numField, btnConsultar, lblStatus,
                sep,
                lblAcao, botoesAcao, msgLabel,
                aviso
        );

        root.getChildren().addAll(titulo, sub, card);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: #f0f2f5;");
        tab.setContent(scroll);
        return tab;
    }

    private void executarBloqueio(TextField numField, boolean bloquear,
                                   Label lblStatus, Label msgLabel) {
        try {
            int num = Integer.parseInt(numField.getText().trim());
            String resp = api.bloquearConta(num, bloquear);

            if (resp.contains("\"sucesso\":true")) {
                String msg = bloquear ? "✔ Conta bloqueada com sucesso." : "✔ Conta desbloqueada com sucesso.";
                msgLabel.setTextFill(Color.web("#16a34a"));
                msgLabel.setText(msg);
                // Atualiza status após ação
                lblStatus.setTextFill(bloquear ? Color.web("#dc2626") : Color.web("#16a34a"));
                lblStatus.setText("Status atualizado: " + (bloquear ? "BLOQUEADA" : "ATIVA"));
            } else {
                msgLabel.setTextFill(Color.web("#dc2626"));
                msgLabel.setText("✘ " + extrairValor(resp, "mensagem"));
            }
        } catch (NumberFormatException ex) {
            msgLabel.setTextFill(Color.web("#dc2626"));
            msgLabel.setText("⚠ Número de conta inválido.");
        }
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
