package pooP2.model;

public class Funcionario extends Pessoa {
    private String cargo;
    private double salario;
    
    public Funcionario() {}
    
    public Funcionario(String nome, String cpf, String email, String cargo, double salario) {
        super(nome, cpf, email);
        this.cargo = cargo;
        setSalario(salario);
    }
    
    @Override
    public String getTipo() {
        return "Funcionario - " + cargo;
    }
    
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    
    public double getSalario() { return salario; }
    public void setSalario(double salario) { 
        if(salario < 0) throw new IllegalArgumentException("Salário não pode ser negativo");
        this.salario = salario; 
    }
}
