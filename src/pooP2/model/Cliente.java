package pooP2.model;

public class Cliente extends Pessoa {
    private double limiteCredito;
    
    public Cliente() {}
    
    public Cliente(String nome, String cpf, String email) {
        super(nome, cpf, email);
        this.limiteCredito = 1000.0;
    }
    
    @Override
    public String getTipo() {
        return "Cliente";
    }
    
    public double getLimiteCredito() { return limiteCredito; }
    public void setLimiteCredito(double limiteCredito) { 
        if(limiteCredito < 0) throw new IllegalArgumentException("Limite não pode ser negativo");
        this.limiteCredito = limiteCredito; 
    }
}
