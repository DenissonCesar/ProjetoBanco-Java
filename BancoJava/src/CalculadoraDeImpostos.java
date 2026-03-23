public class CalculadoraDeImpostos {

    private double total;

    public void registrar(Tributavel tributo) {
        total += tributo.calcularTributo();
    }

    public double getTotal() {
        return total;
    }
}
