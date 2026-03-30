# Banco Java — Full Stack

## Identificação

| Campo      | Valor               |
|------------|---------------------|
| **Nome**   | Denisson César       |
| **Matrícula** | 2631100          |
| **Disciplina** | Projeto de Programação |
| **Professor** | Amaury            |

---

## Arquitetura do Sistema

```
Usuário
  │
  ▼
┌─────────────────────────────┐
│  Frontend JavaFX (Desktop)  │  ← Interface gráfica com abas
│  com HttpClient Java 11+    │
└──────────────┬──────────────┘
               │ HTTP / JSON
               ▼
┌─────────────────────────────┐
│  Backend Spring Boot :8080  │
│  ┌──────────────────────┐   │
│  │     Controller       │   │  ← Recebe e responde requisições REST
│  ├──────────────────────┤   │
│  │      Service         │   │  ← Regras de negócio
│  ├──────────────────────┤   │
│  │     Repository       │   │  ← Acesso ao banco de dados
│  └──────────────────────┘   │
└──────────────┬──────────────┘
               │ JPA / Hibernate
               ▼
┌─────────────────────────────┐
│      SQLite (banco.db)      │  ← Persistência em arquivo local
└─────────────────────────────┘
```

### Divisão em Camadas

- **Controller** — recebe as requisições HTTP, valida a entrada e retorna `RespostaDTO`
- **Service** — concentra todas as regras de negócio (depositar, sacar, bloquear, etc.)
- **Repository** — interface Spring Data JPA que abstrai o acesso ao SQLite
- **Model** — entidades JPA com herança `SINGLE_TABLE` (Conta → ContaCorrente / ContaPoupanca)
- **DTO** — objetos de transferência de dados que desacoplam a API dos modelos internos

### Por que Spring Boot?

Framework padrão de mercado para APIs REST em Java. Elimina configuração manual de servidor, serialização JSON e gerenciamento de dependências.

### Por que SQLite?

Banco leve, sem necessidade de instalação de servidor, ideal para projetos acadêmicos e prototipagem. O banco é criado automaticamente como `banco.db` na raiz do projeto.

---

## Estrutura do Projeto

```
banco-fullstack/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/banco/
│       ├── BancoApiApplication.java
│       ├── controller/
│       │   ├── ContaController.java
│       │   └── AuthController.java
│       ├── service/
│       │   ├── ContaService.java
│       │   └── AuthService.java
│       ├── repository/
│       │   └── ContaRepository.java
│       ├── model/
│       │   ├── Conta.java
│       │   ├── ContaCorrente.java
│       │   └── ContaPoupanca.java
│       └── dto/
│           ├── CriarContaDTO.java
│           ├── OperacaoDTO.java
│           ├── TransferenciaDTO.java
│           ├── BloqueioDTO.java
│           ├── LoginDTO.java
│           └── RespostaDTO.java
├── frontend/
│   ├── pom.xml
│   └── src/main/java/com/bancofx/
│       ├── MainApp.java
│       ├── service/
│       │   └── ApiService.java
│       └── controller/
│           ├── ContaTab.java
│           ├── OperacoesTab.java
│           ├── TributosTab.java
│           ├── BloqueioTab.java
│           └── TopSaldosTab.java
├── README.md
└── DECISOES.md
```

---

## Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.8+

### 1. Backend

```bash
cd banco-fullstack/backend
mvn spring-boot:run
```

O servidor sobe em `http://localhost:8080`.  
O arquivo `banco.db` é criado automaticamente na pasta `backend/`.

### 2. Frontend (Desktop JavaFX)

Em outro terminal:

```bash
cd banco-fullstack/frontend
mvn javafx:run
```

A janela de login abre. Use as credenciais:
- **Nome:** `Admin`
- **Senha:** `admin123`

---

## Endpoints da API

### Contas

| Método | Endpoint              | Descrição                        |
|--------|-----------------------|----------------------------------|
| POST   | `/contas`             | Criar nova conta                 |
| GET    | `/contas`             | Listar todas as contas           |
| GET    | `/contas/{numero}`    | Buscar conta por número          |
| POST   | `/contas/depositar`   | Realizar depósito                |
| POST   | `/contas/sacar`       | Realizar saque                   |
| POST   | `/contas/transferir`  | Transferência entre contas       |
| GET    | `/contas/tributos`    | Calcular tributos de todas as contas |
| POST   | `/contas/bloquear`    | Bloquear ou desbloquear conta    |
| GET    | `/contas/top-saldos`  | Ranking de contas por saldo ⭐   |

### Autenticação

| Método | Endpoint      | Descrição              |
|--------|---------------|------------------------|
| POST   | `/auth/login` | Autenticar o gerente   |

---

## Exemplos de Requisição

### Criar conta
```json
POST /contas
{
  "numero": 101,
  "titular": "João",
  "tipo": "CORRENTE"
}
```

### Depositar
```json
POST /contas/depositar
{
  "numero": 101,
  "valor": 1000.00
}
```

### Transferir
```json
POST /contas/transferir
{
  "origem": 101,
  "destino": 102,
  "valor": 250.00
}
```

### Bloquear conta
```json
POST /contas/bloquear
{
  "numero": 101,
  "bloquear": true
}
```

### Login do gerente
```json
POST /auth/login
{
  "nome": "Admin",
  "senha": "admin123"
}
```

---

## Funcionalidades Implementadas

- ✅ Criar conta (Corrente e Poupança)
- ✅ Listar contas
- ✅ Depositar
- ✅ Sacar
- ✅ Transferir
- ✅ Calcular tributos (polimorfismo mantido da atividade anterior)
- ✅ Autenticar gerente
- ✅ **Bloqueio / Desbloqueio de conta** (funcionalidade adicional)
- ✅ **Endpoint exclusivo:** `GET /contas/top-saldos`

---

## Credenciais do Gerente

Configuradas em `application.properties`:

```properties
banco.gerente.nome=Admin
banco.gerente.senha=admin123
```
