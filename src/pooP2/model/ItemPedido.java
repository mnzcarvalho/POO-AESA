package pooP2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemPedido implements Serializable {
    private Produto produto;
    private int quantidade;
    
    public ItemPedido() {}
    
    public ItemPedido(Produto produto, int quantidade) {
        this.produto = produto;
        setQuantidade(quantidade);
    }
    
    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }
    
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { 
        if(quantidade <= 0) throw new IllegalArgumentException("Quantidade deve ser positiva");
        this.quantidade = quantidade; 
    }
}
