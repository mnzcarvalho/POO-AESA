package pooP2.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.format.DateTimeFormatter;


public class Pedido implements Serializable {
    private static int contador = 1;
    private int id;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private String status;
    private LocalDate dataPedido;


    public Pedido(Cliente cliente) {
        this.id = contador++;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.status = "Pendente";
        this.dataPedido = LocalDate.now();
    }

    public Pedido(Cliente cliente, LocalDate data) {
        this(cliente);
        this.cliente = cliente;
        this.dataPedido = data;
    }


    public void adicionarItem(Produto produto, int quantidade) {
        produto.reduzirEstoque(quantidade);
        itens.add(new ItemPedido(produto, quantidade));
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }


    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getDataFormatada() {
        return dataPedido.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getTotalFormatado() {
        return String.format("R$%.2f", calcularTotal());
    }

    @Override
    public String toString() {
        String nomeCliente = (cliente != null) ? cliente.getNome() : "Cliente não informado";
        return "Pedido #" + id + " - " + nomeCliente +
                " - " + getDataFormatada() + " - Total: " + getTotalFormatado();
    }
}
