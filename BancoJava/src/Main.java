import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Banco banco = new Banco();
        CalculadoraDeImpostos calc = new CalculadoraDeImpostos();
        Gerente gerente = new Gerente("Admin", "admin123");

        int opcao;

        do {
            System.out.println("\n===== BANCO JAVA =====");
            System.out.println("1 - Criar conta");
            System.out.println("2 - Listar contas");
            System.out.println("3 - Depositar");
            System.out.println("4 - Sacar");
            System.out.println("5 - Transferir");
            System.out.println("6 - Consultar saldo");
            System.out.println("7 - Calcular tributo de contas correntes");
            System.out.println("8 - Autenticar gerente");
            System.out.println("9 - Excluir conta");
            System.out.println("0 - Sair");

            System.out.print("Escolha: ");
            opcao = scanner.nextInt();

            switch (opcao) {

                case 1:
                    criarConta(banco);
                    break;

                case 2:
                    listarContas(banco);
                    break;

                case 3:
                    depositar(banco);
                    break;

                case 4:
                    sacar(banco);
                    break;

                case 5:
                    transferir(banco);
                    break;

                case 6:
                    consultarSaldo(banco);
                    break;

                case 7:
                    for (Conta c : banco.getContas()) {
                        if (c instanceof Tributavel) {
                            calc.registrar((Tributavel) c);
                        }
                    }
                    System.out.println("Total imposto: " + calc.getTotal());
                    break;
                case 8:

                    int tentativas = 0;
                    boolean autenticado = false;

                    while (tentativas < 3) {
                        System.out.println("Senha:");
                        String senha = sc.next();

                        if (gerente.autenticar(senha)) {
                            autenticado = true;
                            break;
                        } else {
                            System.out.println("Senha incorreta.");
                            tentativas++;
                        }
                    }

                    if (autenticado) {
                        System.out.println("Acesso permitido");
                    } else {
                        System.out.println("Conta bloqueada por tentativas inválidas");
                    }

                    break;
                case 9:
                    excluirConta(banco);
                    break;
                case 0:
                    System.out.println("Encerrando sistema...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    public static void criarConta(Banco banco) {

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

        banco.adicionarConta(conta);

        System.out.println("Conta criada com sucesso.");
    }

    public static void listarContas(Banco banco) {

        if (banco.getContas().isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }

        for (int i = 0; i < banco.getContas().size(); i++) {
            System.out.println(i + " - " + banco.getContas().get(i));
        }
    }

    public static void depositar(Banco  banco) {

        listarContas(banco);

        System.out.print("Escolha a conta: ");
        int index = scanner.nextInt();

        System.out.print("Valor do depósito: ");
        double valor = scanner.nextDouble();

        banco.getContas().get(index).depositar(valor);

        System.out.println("Depósito realizado.");
    }

    public static void sacar(Banco banco) {

        listarContas(banco);

        System.out.print("Escolha a conta: ");
        int index = scanner.nextInt();

        if (banco.getContas().size() <= index) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }

        System.out.print("Valor do saque: ");
        double valor = scanner.nextDouble();

        banco.getContas().get(index).sacar(valor);
    }

    public static void transferir(Banco banco) {

        listarContas(banco);

        System.out.print("Conta origem: ");
        int origem = scanner.nextInt();

        System.out.print("Conta destino: ");
        int destino = scanner.nextInt();

        System.out.print("Valor da transferência: ");
        double valor = scanner.nextDouble();

        banco.getContas().get(origem).transferir(banco.getContas().get(destino), valor);

        System.out.println("Transferência realizada.");
    }

    public static void consultarSaldo(Banco banco) {

        listarContas(banco);

        System.out.print("Escolha a conta: ");
        int index = scanner.nextInt();

        System.out.println("Saldo: " + banco.getContas().get(index).getSaldo());
    }

    public static void excluirConta(Banco banco) {

        listarContas(banco);

        System.out.print("Escolha a conta: ");
        int index = scanner.nextInt();

        if (banco.getContas().size() <= index) {
            System.out.println("Nenhuma conta cadastrada.");
        }

        Conta conta;
        banco.getContas().remove(index);

        System.out.print("Conta excluída com sucesso.");
    }
}