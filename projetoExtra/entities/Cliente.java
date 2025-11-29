package projetoExtra.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;


    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime dataCadastro;
    private List<Endereco> enderecos; // Relacionamento 1:N
    private List<Categoria> categorias; // Relacionamento N:N


    public Cliente() {
        this.enderecos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.dataCadastro = LocalDateTime.now();
    }

    public Cliente(String nome, String email, String telefone) {
        this();
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }


    public abstract String getDocumento();
    public abstract void validarDocumento() throws Exception;
    public abstract String getTipoCliente();


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (nome.length() < 2) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 2 caracteres");
        }
        this.nome = nome;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) {
        if (email != null && !email.isEmpty() && !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.email = email;
    }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }


    public List<Endereco> getEnderecos() { return enderecos; }
    public void setEnderecos(List<Endereco> enderecos) { this.enderecos = enderecos; }

    public void adicionarEndereco(Endereco endereco) {
        if (endereco != null && !enderecos.contains(endereco)) {
            enderecos.add(endereco);
        }
    }

    public void removerEndereco(Endereco endereco) {
        enderecos.remove(endereco);
    }


    public List<Categoria> getCategorias() { return categorias; }
    public void setCategorias(List<Categoria> categorias) { this.categorias = categorias; }

    public void adicionarCategoria(Categoria categoria) {
        if (categoria != null && !categorias.contains(categoria)) {
            categorias.add(categoria);
        }
    }

    public void removerCategoria(Categoria categoria) {
        categorias.remove(categoria);
    }

    public boolean temCategoria(String nomeCategoria) {
        for (Categoria cat : categorias) {
            if (cat.getNome().equalsIgnoreCase(nomeCategoria)) {
                return true;
            }
        }
        return false;
    }


    public String getInformacoesContato() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome: ").append(nome);
        if (email != null && !email.isEmpty()) {
            sb.append(", Email: ").append(email);
        }
        if (telefone != null && !telefone.isEmpty()) {
            sb.append(", Tel: ").append(telefone);
        }
        return sb.toString();
    }


    @Override
    public String toString() {
        return String.format("%s [ID: %d, Tipo: %s, Documento: %s]",
                nome, id, getTipoCliente(), getDocumento());
    }
}