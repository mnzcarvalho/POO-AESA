package pooP2.model;

public class Gerente extends Funcionario {
    private String departamento;
    
    public Gerente() {}
    
    public Gerente(String nome, String cpf, String email, double salario, String departamento) {
        super(nome, cpf, email, "Gerente", salario);
        this.departamento = departamento;
    }
    
    @Override
    public String getTipo() {
        return "Gerente do " + departamento;
    }
    
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}
