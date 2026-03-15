import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static ArrayList<Conta> contas = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int opcao;

        do {
            System.out.println("\n===== BANCO JAVA =====");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Listar contas");
            System.out.println("3 - Depositar");
            System.out.println("4 - Sacar");
            System.out.println("5 - Transferir");
            System.out.println("6 - Consultar saldo");
            System.out.println("7 - Excluir conta");
            System.out.println("0 - Sair");

            System.out.print("Escolha: ");
            opcao = scanner.nextInt();

            switch (opcao) {

                case 1:
                    criarConta();
                    break;

                case 2:
                    listarContas();
                    break;

                case 3:
                    depositar();
                    break;

                case 4:
                    sacar();
                    break;

                case 5:
                    transferir();
                    break;

                case 6:
                    consultarSaldo();
                    break;
                case 7:
                    excluirConta();

                case 0:
                    System.out.println("Encerrando sistema...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    public static void criarConta() {

        System.out.println("\nTipo de conta:");
        System.out.println("1 - Corrente");
        System.out.println("2 - Poupança");

        int tipo = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Nome do titular: ");
        String titular = scanner.nextLine();

        Conta conta;

        if (tipo == 1) {
            conta = new ContaCorrente(titular);
        } else {
            conta = new ContaPoupanca(titular);
        }

        contas.add(conta);

        System.out.println("Conta criada com sucesso.");
    }

    public static void listarContas() {

        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }

        for (int i = 0; i < contas.size(); i++) {
            System.out.println(i + " - " + contas.get(i));
        }
    }

    public static void depositar() {

        listarContas();

        System.out.print("Escolha a conta: ");
        int index = scanner.nextInt();

        System.out.print("Valor do depósito: ");
        double valor = scanner.nextDouble();

        contas.get(index).depositar(valor);

        System.out.println("Depósito realizado.");
    }

    public static void sacar() {

        listarContas();

        System.out.print("Escolha a conta: ");
        int index = scanner.nextInt();

        if (contas.size() <= index) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }

        System.out.print("Valor do saque: ");
        double valor = scanner.nextDouble();

        contas.get(index).sacar(valor);
    }

    public static void transferir() {

        listarContas();

        System.out.print("Conta origem: ");
        int origem = scanner.nextInt();

        System.out.print("Conta destino: ");
        int destino = scanner.nextInt();

        System.out.print("Valor da transferência: ");
        double valor = scanner.nextDouble();

        contas.get(origem).transferir(contas.get(destino), valor);

        System.out.println("Transferência realizada.");
    }

    public static void consultarSaldo() {

        listarContas();

        System.out.print("Escolha a conta: ");
        int index = scanner.nextInt();

        System.out.println("Saldo: " + contas.get(index).getSaldo());
    }

    public static void excluirConta() {

        listarContas();

        System.out.print("Escolha a conta: ");
        int index = scanner.nextInt();

        if (contas.size() <= index) {
            System.out.println("Nenhuma conta cadastrada.");
        }

        Conta conta;
        contas.remove(index);

        System.out.print("Conta excluída com sucesso.");
    }
}