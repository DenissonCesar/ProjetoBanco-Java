public class ContaCorrente extends Conta implements Tributavel {

    public ContaCorrente(String titular) {
        super(titular);
    }

    @Override
    public double calcularTributo() {
        return this.getSaldo() * 0.1;
    }
}

