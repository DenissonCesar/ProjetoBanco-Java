import java.util.ArrayList;
import java.util.List;

public class Banco {

    private ArrayList<Conta> contas = new ArrayList<>();

    public void adicionarConta(Conta conta) {
        contas.add(conta);
    }

    public Conta buscarConta(int numero) {
        for (Conta conta : contas) {
            if (conta.getNumero() == numero) {
                return conta;
            }
        }
        return null;
    }

    public void listarContas() {
        for (Conta conta : contas) {
            System.out.println("Conta: " + conta.getNumero() + " Saldo: " + conta.getSaldo());
        }
    }

    public ArrayList<Conta> getContas() {
        return contas;
    }

    public void excluirConta(Conta conta) {
        contas.remove(conta);
    }


}
