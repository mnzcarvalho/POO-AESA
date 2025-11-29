package projetoExtra.entities;

import java.io.Serializable;

public class Categoria implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String descricao;

    // Construtores
    public Categoria() {}

    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser vazio");
        }
        this.nome = nome;
    }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return nome + (descricao != null ? " - " + descricao : "");
    }
}