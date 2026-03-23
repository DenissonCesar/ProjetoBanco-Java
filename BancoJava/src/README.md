# Sistema Bancário em Java

## Identificação

Nome: Denisson César
Matrícula: 2631100
Disciplina: Projeto de Programação
Professor: Amaury

---

# Explicação da Arquitetura

## Por que `Conta` é abstrata?

A classe `Conta` é abstrata porque representa um conceito genérico de conta bancária. No sistema, não existe uma conta “genérica”, apenas tipos específicos como Conta Corrente e Conta Poupança.

A abstração permite reutilizar código comum (saldo, depósito, saque) e evita a criação de objetos que não fazem sentido no contexto do sistema.

---

## Por que `Tributavel` é interface?

A interface `Tributavel` representa um comportamento: pagar tributo.

Ela define o método:

```java
double calcularTributo();
```

Foi utilizada interface porque nem todas as contas pagam imposto. Dessa forma, apenas as classes que precisam desse comportamento implementam a interface, garantindo flexibilidade e organização no código.

---

## Como ocorre o polimorfismo no cálculo de imposto?

O polimorfismo ocorre na classe `CalculadoraDeImpostos`, no método:

```java
registrar(Tributavel t)
```

Esse método recebe qualquer objeto que implemente a interface `Tributavel`.

Exemplo:

```java
Conta c = new ContaCorrente("João");

if (c instanceof Tributavel) {
    calculadora.registrar((Tributavel) c);
}
```

A calculadora não precisa conhecer o tipo específico da conta, apenas que ela implementa `Tributavel`. Cada objeto calcula seu próprio tributo, caracterizando o uso de polimorfismo.

---

# Execução do Sistema

## Compilar

No terminal, dentro da pasta do projeto:

```bash
javac *.java
```

## Executar

```bash
java Main
```

---

# Exemplo de Execução

```
===== BANCO JAVA =====
1 - Criar conta
2 - Listar contas
3 - Depositar
4 - Sacar
5 - Transferir
6 - Consultar saldo
7 - Calcular tributo de contas correntes
8 - Autenticar gerente
9 - Excluir conta
0 - Sair

Escolha: 1

Tipo de conta:
1 - Corrente
2 - Poupança
1
Nome do titular: João

Conta criada com sucesso.

Escolha: 2
0 - Títular da Conta: João | Saldo: 0.0

Escolha: 3
Escolha a conta: 0
Valor do depósito: 1000

Depósito realizado.

Escolha: 7
Total imposto: 100.0
