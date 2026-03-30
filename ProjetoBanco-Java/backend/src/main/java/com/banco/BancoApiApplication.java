package com.banco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BancoApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(BancoApiApplication.class, args);
        System.out.println("======================================");
        System.out.println("  BANCO API RODANDO em :8080");
        System.out.println("  Banco de dados: banco.db (SQLite)");
        System.out.println("======================================");
    }
}
