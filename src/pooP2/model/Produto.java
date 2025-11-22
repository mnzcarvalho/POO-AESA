package pooP2.model;

import pooP2.exception.EstoqueInsuficienteException;

import java.io.Serializable;


public class Produto implements Serializable {
    private static int contador = 1;
    private int id;
    private String nome;
    private double preco;
    private int estoque;
    
    public Produto() {
        this.id = contador++;
    }
    
    public Produto(String nome, double preco, int estoque) {
        this();
        this.nome = nome;
        setPreco(preco);
        setEstoque(estoque);
    }
    
    public int getId() { return id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public double getPreco() { return preco; }
    public void setPreco(double preco) { 
        if(preco < 0) throw new IllegalArgumentException("Preço não pode ser negativo");
        this.preco = preco; 
    }
    
    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { 
        if(estoque < 0) throw new IllegalArgumentException("Estoque não pode ser negativo");
        this.estoque = estoque; 
    }

    public void reduzirEstoque(int quantidade) {
        if(quantidade > estoque) {
            throw new EstoqueInsuficienteException("Estoque insuficiente. Disponível: " + estoque);
        }
        estoque -= quantidade;
    }

    @Override
    public String toString() {
        return String.format("%d - %s - R$%.2f - Estoque: %d", id, nome, preco, estoque);
    }
}
