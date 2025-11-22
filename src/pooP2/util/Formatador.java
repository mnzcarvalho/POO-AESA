package pooP2.util;

public class Formatador {
    public static String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
    
    public static String formatarMoeda(double valor) {
        return String.format("R$%.2f", valor);
    }
}
