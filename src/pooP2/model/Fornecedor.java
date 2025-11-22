package pooP2.model;

public class Fornecedor extends Pessoa {
    private String categoria;
    
    public Fornecedor() {}
    
    public Fornecedor(String nome, String cpf, String email, String categoria) {
        super(nome, cpf, email);
        this.categoria = categoria;
    }
    
    @Override
    public String getTipo() {
        return "Fornecedor - " + categoria;
    }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
