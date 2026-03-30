package com.bancofx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import com.bancofx.service.ApiService;
import com.bancofx.controller.*;

/**
 * Aplicação JavaFX — Sistema Bancário Desktop
 * Consome a API REST do backend Spring Boot.
 */
public class MainApp extends Application {

    private ApiService apiService = new ApiService();
    private boolean autenticado   = false;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Banco Java — Sistema Bancário");
        stage.setWidth(900);
        stage.setHeight(680);
        stage.setResizable(false);

        // Tela de login antes de abrir o sistema
        mostrarLogin(stage);
    }

    // ── Tela de Login ──────────────────────────────────────────────────────────
    private void mostrarLogin(Stage stage) {
        VBox root = new VBox(18);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label titulo = new Label("BANCO JAVA");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titulo.setTextFill(Color.web("#e0e0e0"));

        Label sub = new Label("Acesso do Gerente");
        sub.setFont(Font.font("Arial", 14));
        sub.setTextFill(Color.web("#aaaaaa"));

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome do gerente");
        nomeField.setMaxWidth(300);
        nomeField.setStyle(estiloInput());

        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText("Senha");
        senhaField.setMaxWidth(300);
        senhaField.setStyle(estiloInput());

        Label msgLabel = new Label("");
        msgLabel.setTextFill(Color.web("#ff6b6b"));

        Button btnLogin = new Button("ENTRAR");
        btnLogin.setMaxWidth(300);
        btnLogin.setStyle(estiloBotaoPrimario());

        btnLogin.setOnAction(e -> {
            String nome  = nomeField.getText().trim();
            String senha = senhaField.getText().trim();

            if (nome.isEmpty() || senha.isEmpty()) {
                msgLabel.setText("Preencha nome e senha.");
                return;
            }

            String resposta = apiService.login(nome, senha);
            if (resposta.contains("\"sucesso\":true")) {
                autenticado = true;
                mostrarSistema(stage);
            } else {
                msgLabel.setText("⚠ Credenciais inválidas. Tente novamente.");
            }
        });

        // Enter no campo senha também faz login
        senhaField.setOnAction(e -> btnLogin.fire());

        root.getChildren().addAll(titulo, sub, nomeField, senhaField, btnLogin, msgLabel);
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ── Sistema Principal (Abas) ───────────────────────────────────────────────
    private void mostrarSistema(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f2f5;");

        // Header
        HBox header = new HBox();
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: #1a1a2e;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label titulo = new Label("BANCO JAVA  —  Sistema Bancário");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titulo.setTextFill(Color.web("#e0e0e0"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label badge = new Label("✔ Gerente autenticado");
        badge.setFont(Font.font("Arial", 12));
        badge.setTextFill(Color.web("#4caf50"));

        header.getChildren().addAll(titulo, spacer, badge);

        // TabPane com todas as funcionalidades
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setStyle("-fx-background-color: #f0f2f5;");

        tabPane.getTabs().addAll(
            new ContaTab(apiService).criarTab(),
            new OperacoesTab(apiService).criarTab(),
            new TributosTab(apiService).criarTab(),
            new BloqueioTab(apiService).criarTab(),
            new TopSaldosTab(apiService).criarTab()
        );

        root.setTop(header);
        root.setCenter(tabPane);

        stage.setScene(new Scene(root));
    }

    // ── Estilos ────────────────────────────────────────────────────────────────
    public static String estiloInput() {
        return "-fx-background-color: #2a2a4a; " +
               "-fx-text-fill: #e0e0e0; " +
               "-fx-prompt-text-fill: #888888; " +
               "-fx-border-color: #4a4a8a; " +
               "-fx-border-radius: 6; " +
               "-fx-background-radius: 6; " +
               "-fx-padding: 8 12 8 12; " +
               "-fx-font-size: 14;";
    }

    public static String estiloBotaoPrimario() {
        return "-fx-background-color: #4f46e5; " +
               "-fx-text-fill: white; " +
               "-fx-font-weight: bold; " +
               "-fx-font-size: 14; " +
               "-fx-padding: 10 20 10 20; " +
               "-fx-background-radius: 6; " +
               "-fx-cursor: hand;";
    }

    public static String estiloBotaoPerigo() {
        return "-fx-background-color: #dc2626; " +
               "-fx-text-fill: white; " +
               "-fx-font-weight: bold; " +
               "-fx-font-size: 13; " +
               "-fx-padding: 8 16 8 16; " +
               "-fx-background-radius: 6; " +
               "-fx-cursor: hand;";
    }

    public static String estiloBotaoSucesso() {
        return "-fx-background-color: #16a34a; " +
               "-fx-text-fill: white; " +
               "-fx-font-weight: bold; " +
               "-fx-font-size: 13; " +
               "-fx-padding: 8 16 8 16; " +
               "-fx-background-radius: 6; " +
               "-fx-cursor: hand;";
    }

    public static String estiloCard() {
        return "-fx-background-color: white; " +
               "-fx-background-radius: 10; " +
               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
