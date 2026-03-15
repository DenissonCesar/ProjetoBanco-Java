public class Main {
    public static void main(String[] args) {
        Banco banco = new Banco();

        ContaCorrente cc = new ContaCorrente("Denisson");
        ContaPoupanca cp = new ContaPoupanca("Aumary");

        cc.depositar(1000);
        cc.depositar(1000);

        banco.adicionarConta(cc);
        banco.adicionarConta(cp);

        banco.listarContas();
    }
}