# Sistema Bancário em Java

## IDENTIFICAÇÃO

**Nome:** Denisson César
**Matrícula:** 2631100
**Disciplina:** Projeto de Programação
**Professor:** Amaury

---

# DESCRIÇÃO DO PROJETO

Este projeto consiste em um **sistema bancário simples desenvolvido em Java**, utilizando os princípios de **Programação Orientada a Objetos (POO)**.

O sistema permite criar e gerenciar contas bancárias do tipo **Conta Corrente** e **Conta Poupança**, possibilitando operações básicas como depósitos, saques, transferências, consulta de saldo e exclusão de contas.

Todas as contas são armazenadas em uma **lista dinâmica (`ArrayList`)**, permitindo gerenciamento simples e organizado dentro da aplicação.

---

# COMO EXECUTAR

1. Compile todos os arquivos `.java` do projeto.
2. Execute a classe `Main`.
3. O sistema exibirá um menu interativo no terminal.
4. Digite o número da operação desejada e pressione **Enter**.
5. O sistema continuará rodando até que a opção **0 - Sair** seja escolhida.

---

# FUNCIONALIDADES IMPLEMENTADAS

* Criar conta
* Listar contas
* Depositar
* Sacar
* Transferir
* Consultar saldo
* Excluir conta

---

# EXEMPLO DE EXECUÇÃO

```
===== BANCO JAVA =====
1 - Criar conta
2 - Listar contas
3 - Depositar
4 - Sacar
5 - Transferir
6 - Consultar saldo
7 - Excluir conta
0 - Sair

Escolha: 1

Tipo de conta:
1 - Corrente
2 - Poupança

Nome do titular: Denisson

Conta criada com sucesso.

===== BANCO JAVA =====
1 - Criar conta
2 - Listar contas
3 - Depositar
4 - Sacar
5 - Transferir
6 - Consultar saldo
7 - Excluir conta
0 - Sair

Escolha: 3

0 - Titular: Denisson | Saldo: 0.0
Escolha a conta: 0

Valor do depósito: 500

Depósito realizado.

===== BANCO JAVA =====
Escolha: 6

0 - Titular: Denisson | Saldo: 500.0
Escolha a conta: 0

Saldo atual: 500.0

===== BANCO JAVA =====
Escolha: 7

0 - Titular: Denisson | Saldo: 500.0
Escolha a conta para excluir: 0

Conta removida com sucesso.
```

---

# PADRONIZAÇÃO DO CÓDIGO

Para permitir análise automatizada conforme solicitado na atividade:

* O programa inicia pela classe **Main**
* A função principal utilizada é:

```
public static void main(String[] args)
```

* O menu é executado dentro de um **loop `while`**, permitindo múltiplas operações até que o usuário escolha sair.
* As contas são armazenadas utilizando a estrutura:

```
ArrayList<Conta>
```

* Cada operação do sistema foi separada em métodos específicos para melhor organização do código.

Exemplo:

```
criarConta()
listarContas()
depositar()
sacar()
transferir()
consultarSaldo()
excluirConta()
```

---

# ESTRUTURA DO PROJETO

```
Conta.java
ContaCorrente.java
ContaPoupanca.java
Main.java
```

---

# CONCEITOS UTILIZADOS

O projeto utiliza diversos conceitos fundamentais de **Programação Orientada a Objetos**:

* **Classe Abstrata**
  `Conta` foi implementada como classe abstrata para servir de base para outros tipos de conta.

* **Herança**
  `ContaCorrente` e `ContaPoupanca` herdam da classe `Conta`.

* **Polimorfismo**
  A lista `ArrayList<Conta>` permite armazenar diferentes tipos de conta.

* **Encapsulamento**
  O acesso e manipulação do saldo são controlados por métodos da própria classe.

* **Coleções em Java**
  Utilização de `ArrayList` para armazenar dinamicamente as contas criadas.
