package pooP2.model;

import pooP2.exception.CPFInvalidoException;
import pooP2.util.ValidacaoCPF;

import java.io.Serializable;


public abstract class Pessoa implements Serializable {
    private String nome;
    private String cpf;
    private String email;
    
    
    public Pessoa() {}
    
    public Pessoa(String nome, String cpf, String email) {
        this.nome = nome;
        setCpf(cpf);
        this.email = email;
    }
    
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) {
        if(!ValidacaoCPF.validar(cpf)) {
            throw new CPFInvalidoException("CPF inválido: " + cpf);
        }
        this.cpf = cpf;
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    
    public abstract String getTipo();
    
    @Override
    public String toString() {
        return nome + " (" + cpf + ")";
    }
}
