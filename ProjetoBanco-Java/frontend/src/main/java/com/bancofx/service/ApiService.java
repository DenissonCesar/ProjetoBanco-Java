package com.bancofx.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Serviço responsável por toda comunicação HTTP com a API REST.
 * Usa HttpClient nativo do Java 11+ (sem dependências extras).
 */
public class ApiService {

    private static final String BASE_URL = "http://localhost:8080";
    private final HttpClient client;

    public ApiService() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    // ── Métodos auxiliares ─────────────────────────────────────────────────────

    private String post(String endpoint, String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "{\"sucesso\":false,\"mensagem\":\"Erro de conexão: " + e.getMessage() + "\"}";
        }
    }

    private String get(String endpoint) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .header("Content-Type", "application/json")
                    .GET()
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "{\"sucesso\":false,\"mensagem\":\"Erro de conexão: " + e.getMessage() + "\"}";
        }
    }

    // ── Endpoints da API ───────────────────────────────────────────────────────

    public String login(String nome, String senha) {
        String body = String.format("{\"nome\":\"%s\",\"senha\":\"%s\"}", nome, senha);
        return post("/auth/login", body);
    }

    public String criarConta(int numero, String titular, String tipo) {
        String body = String.format(
                "{\"numero\":%d,\"titular\":\"%s\",\"tipo\":\"%s\"}",
                numero, titular, tipo
        );
        return post("/contas", body);
    }

    public String listarContas() {
        return get("/contas");
    }

    public String buscarConta(int numero) {
        return get("/contas/" + numero);
    }

    public String depositar(int numero, double valor) {
        String body = String.format("{\"numero\":%d,\"valor\":%.2f}", numero, valor)
                .replace(",", ".");
        // garante ponto decimal independente do locale
        body = String.format("{\"numero\":%d,\"valor\":%.2f}", numero, valor);
        return post("/contas/depositar", body);
    }

    public String sacar(int numero, double valor) {
        String body = String.format("{\"numero\":%d,\"valor\":%.2f}", numero, valor);
        return post("/contas/sacar", body);
    }

    public String transferir(int origem, int destino, double valor) {
        String body = String.format(
                "{\"origem\":%d,\"destino\":%d,\"valor\":%.2f}",
                origem, destino, valor
        );
        return post("/contas/transferir", body);
    }

    public String calcularTributos() {
        return get("/contas/tributos");
    }

    public String bloquearConta(int numero, boolean bloquear) {
        String body = String.format("{\"numero\":%d,\"bloquear\":%b}", numero, bloquear);
        return post("/contas/bloquear", body);
    }

    public String topSaldos() {
        return get("/contas/top-saldos");
    }
}
