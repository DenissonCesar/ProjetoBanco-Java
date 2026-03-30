# DECISOES.md — Log de Decisões Arquiteturais

## Identificação

**Nome:** Denisson César  
**Matrícula:** 2631100

---

## 1. Por que SINGLE_TABLE para herança JPA?

Optei pela estratégia `@Inheritance(strategy = InheritanceType.SINGLE_TABLE)` em vez de `JOINED` pelos seguintes motivos:

- **Performance:** `SINGLE_TABLE` faz apenas um `SELECT` por consulta, sem JOINs entre tabelas
- **Simplicidade:** como `ContaCorrente` e `ContaPoupanca` compartilham quase todos os campos (número, titular, saldo, bloqueada), não há desperdício significativo de colunas nulas
- **Adequação ao escopo:** em um sistema bancário acadêmico com dois subtipos, o ganho de `JOINED` (normalização) não compensa a complexidade adicionada

**Tradeoff aceito:** a coluna `tipo` fica como discriminador e campos exclusivos de subtipos futuros causariam colunas nulas, mas isso não ocorre no projeto atual.

---

## 2. Por que JavaFX para o frontend?

A atividade permitia escolher entre interface Web (HTML/JS) ou Desktop (JavaFX/Swing). Escolhi **JavaFX** por:

- **Integração natural com Java:** todo o código permanece na mesma linguagem, sem necessidade de aprender frameworks JavaScript
- **HttpClient nativo (Java 11+):** não precisei de bibliotecas externas para consumir a API REST — o `java.net.http.HttpClient` faz o trabalho com facilidade
- **Aparência moderna:** JavaFX permite aplicar CSS inline, criando uma interface mais profissional que Swing sem a complexidade de um framework web separado

**Dificuldade encontrada:** configurar o plugin `javafx-maven-plugin` para resolver corretamente os módulos JavaFX exigiu atenção especial à versão do JDK (17+) e ao `module-info`.

---

## 3. Por que DTOs separados dos Models?

Decidi criar DTOs (Data Transfer Objects) distintos das entidades JPA porque:

- Evita expor campos internos do banco (como `id` JPA ou `tipo` discriminador)
- Permite que a API evolua sem impactar a estrutura do banco e vice-versa
- Facilita validações de entrada sem poluir as entidades
- Segue o padrão de mercado para APIs REST profissionais

---

## 4. Decisão: Bloqueio de Conta como funcionalidade adicional

Escolhi **bloqueio de conta** como funcionalidade adicional porque:

- É uma funcionalidade realista de sistemas bancários reais (ex.: bloqueio por suspeita de fraude)
- Demonstra extensão do modelo (campo `bloqueada` na entidade) e regra de negócio transversal (verificada em depósito, saque e transferência)
- Integra perfeitamente com o conceito de gerente/autenticação já presente na atividade anterior

**Como foi implementado:**
- Campo `boolean bloqueada` na entidade `Conta` (persistido no SQLite)
- Verificação no início de cada operação (`depositar`, `sacar`, `transferir`)
- Endpoint `POST /contas/bloquear` com `BloqueioDTO` contendo `numero` e `bloquear` (boolean)
- Aba dedicada na interface JavaFX com consulta de status e botões de ação

---

## 5. Endpoint exclusivo: GET /contas/top-saldos

Criei o endpoint `GET /contas/top-saldos` que retorna todas as contas ordenadas pelo saldo de forma decrescente. A implementação usa uma `@Query` JPQL no repositório:

```java
@Query("SELECT c FROM Conta c ORDER BY c.saldo DESC")
List<Conta> findAllOrderBySaldoDesc();
```

Escolhi esse endpoint porque é útil para o gerente identificar rapidamente as contas com maior movimentação financeira, simulando funcionalidades de relatório presentes em sistemas bancários reais.

---

## 6. Dificuldades encontradas

| Dificuldade | Como resolvi |
|---|---|
| SQLite não tem dialect nativo no Hibernate 6 | Adicionei a dependência `hibernate-community-dialects` e usei `SQLiteDialect` dela |
| JavaFX requer módulos explícitos no Java 17 | Configurei o plugin `javafx-maven-plugin` e adicionei as dependências corretas no `pom.xml` |
| Serialização de herança com Jackson | Jackson serializa a entidade concreta corretamente sem configuração extra graças ao discriminador JPA |
| Locale do `String.format` com vírgula decimal | Usei `.replace(",", ".")` no frontend para garantir ponto decimal independente do sistema operacional |

---

## 7. O que mantive da atividade anterior

- **Herança:** `Conta` abstrata → `ContaCorrente` e `ContaPoupanca`
- **Polimorfismo:** `calcularTributo()` implementado diferente em cada subclasse (ContaCorrente: 10%, ContaPoupanca: 0%)
- **Autenticação:** lógica de `Gerente.autenticar()` migrada para `AuthService`, com credenciais configuráveis
- **Regras de saldo insuficiente:** mantidas como exceções (`IllegalStateException`) no model

---

## 8. O que evoluiu em relação à atividade anterior

| Antes | Agora |
|---|---|
| `ArrayList<Conta>` em memória | Persistência real no SQLite via JPA |
| Menu no terminal (Scanner) | Interface gráfica JavaFX com abas |
| Lógica misturada no `Main.java` | Separação em Controller / Service / Repository |
| Sem API | API REST com endpoints documentados |
| Sem DTOs | DTOs de entrada e saída desacoplados |
