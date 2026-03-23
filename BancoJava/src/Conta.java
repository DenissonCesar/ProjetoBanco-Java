public abstract class Conta {
    protected double saldo;
    protected int numero;
    protected String titular;
    private static int contador = 1;


    public Conta(String titular) {
        this.saldo = 0;
        this.numero = contador++;
        this.titular = titular;
    }

    public void depositar(double valor) {
        saldo += valor;
    }

    public void sacar(double valor) {
        if (valor <= saldo) {
            saldo -= valor;
        } else {
            System.out.println("Saldo insuficiente.");
        }

    }

    public void transferir(Conta destino, double valor) {
        if (valor <= saldo) {
            this.sacar(valor);
            destino.depositar(valor);
        } else {
            System.out.println("Saldo insuficiente para transação.");
        }

    }

    public double getSaldo() {
        return saldo;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "Títular da Conta: " + titular + "| Saldo: " + saldo;
    }

}

