package pooP2.model;

import pooP2.exception.*;
import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Loja implements Serializable {
    private List<Pessoa> pessoas;
    private List<Produto> produtos;
    private List<Pedido> pedidos;
    private Random random;

    public Loja() {
        this.pessoas = new ArrayList<>();
        this.produtos = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.random = new Random();
    }


    public void adicionarCliente(Cliente cliente) {
        pessoas.add(cliente);
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    public void adicionarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public Cliente buscarCliente(String cpf) {
        for(Pessoa pessoa : pessoas) {
            if(pessoa instanceof Cliente && pessoa.getCpf().equals(cpf)) {
                return (Cliente) pessoa;
            }
        }
        throw new ClienteNaoEncontradoException("Cliente não encontrado: " + cpf);
    }

    public Produto buscarProduto(int id) {
        for(Produto produto : produtos) {
            if(produto.getId() == id) {
                return produto;
            }
        }
        throw new ProdutoNaoEncontradoException("Produto não encontrado: " + id);
    }

    public void listarClientes() {
        System.out.println("\n--- CLIENTES ---");
        for(Pessoa pessoa : pessoas) {
            if(pessoa instanceof Cliente) {
                System.out.println(pessoa);
            }
        }
    }

    public void listarProdutos() {
        System.out.println("\n--- PRODUTOS ---");
        for(Produto produto : produtos) {
            System.out.println(produto);
        }
    }

    public void listarPedidos() {
        System.out.println("\n--- PEDIDOS (ORDENADOS POR DATA) ---");
        pedidos.sort((p1, p2) -> p2.getDataPedido().compareTo(p1.getDataPedido()));
        for(Pedido pedido : pedidos) {
            System.out.println(pedido);
        }
    }

    public void gerarRelatorioVendas() {
        System.out.println("\n--- RELATÓRIO DE VENDAS ---");
        double totalVendas = 0;

        for(Pedido pedido : pedidos) {
            double totalPedido = pedido.calcularTotal();
            totalVendas += totalPedido;
            System.out.println(pedido);
        }

        System.out.printf("TOTAL DE VENDAS: R$%.2f%n", totalVendas);
        System.out.println("TOTAL DE PEDIDOS: " + pedidos.size());
    }


    public void gerarDadosFicticios() {
        System.out.println("Gerando dados dos últimos 6 meses...");

        // Se já existem dados, não sobrescrever clientes e produtos
        if(pessoas.isEmpty()) {
            gerarClientesFicticios();
        }
        if(produtos.isEmpty()) {
            gerarProdutosFicticios();
        }

        gerarPedidos6Meses();

        System.out.println("Dados fictícios gerados com sucesso! " + pedidos.size() + " pedidos criados.");
    }

    private void gerarClientesFicticios() {
        String[] nomes = {"João Silva", "Maria Santos", "Pedro Oliveira", "Ana Costa",
                "Carlos Souza", "Juliana Lima", "Roberto Alves", "Fernanda Rocha"};
        String[] cpfs = {"11122233344", "22233344455", "33344455566", "44455566677",
                "55566677788", "66677788899", "77788899900", "88899900011"};

        for (int i = 0; i < nomes.length; i++) {
            try {
                Cliente cliente = new Cliente(nomes[i], cpfs[i],
                        nomes[i].toLowerCase().replace(" ", ".") + "@email.com");
                pessoas.add(cliente);
            } catch (CPFInvalidoException e) {
                System.out.println("Erro ao gerar cliente: " + e.getMessage());
            }
        }
    }

    private void gerarProdutosFicticios() {
        String[][] produtosDados = {
                {"Notebook Dell", "2599.99", "50"},
                {"Smartphone Samsung", "1899.99", "100"},
                {"Tablet iPad", "3299.99", "30"},
                {"Fone de Ouvido", "299.99", "200"},
                {"Teclado Mecânico", "399.99", "80"},
                {"Mouse Gamer", "199.99", "150"},
                {"Monitor 24\"", "899.99", "40"},
                {"Impressora Laser", "699.99", "25"}
        };

        for (String[] dados : produtosDados) {
            Produto produto = new Produto(dados[0],
                    Double.parseDouble(dados[1]),
                    Integer.parseInt(dados[2]));
            produtos.add(produto);
        }
    }

    private void gerarPedidos6Meses() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataInicio = hoje.minusMonths(6);


        int totalPedidos = 20 + random.nextInt(11); // 20 a 30 pedidos

        for (int i = 0; i < totalPedidos; i++) {

            LocalDate dataPedido = gerarDataAleatoria(dataInicio, hoje);


            Cliente cliente = getClienteAleatorio();


            Pedido pedido = new Pedido(cliente, dataPedido);


            int qtdItens = 1 + random.nextInt(5);
            for (int j = 0; j < qtdItens; j++) {
                try {
                    Produto produto = getProdutoAleatorio();
                    int quantidade = 1 + random.nextInt(3); // 1-3 unidades

                    // Verificar estoque antes de adicionar
                    if (produto.getEstoque() >= quantidade) {
                        pedido.adicionarItem(produto, quantidade);
                    } else {
                        j--; // Tenta outro produto se não tem estoque
                    }
                } catch (EstoqueInsuficienteException e) {
                    j--; // Tenta outro produto
                }
            }

            // Só adiciona pedido se tiver itens
            if (!pedido.getItens().isEmpty()) {
                pedidos.add(pedido);
            }
        }
    }

    private LocalDate gerarDataAleatoria(LocalDate inicio, LocalDate fim) {
        long dias = java.time.temporal.ChronoUnit.DAYS.between(inicio, fim);
        long diasAleatorios = random.nextInt((int) dias + 1);
        return inicio.plusDays(diasAleatorios);
    }

    private Cliente getClienteAleatorio() {
        List<Cliente> clientes = new ArrayList<>();
        for (Pessoa pessoa : pessoas) {
            if (pessoa instanceof Cliente) {
                clientes.add((Cliente) pessoa);
            }
        }
        return clientes.get(random.nextInt(clientes.size()));
    }

    private Produto getProdutoAleatorio() {
        return produtos.get(random.nextInt(produtos.size()));
    }


    public void gerarRelatorioMensal() {
        System.out.println("\n=== RELATÓRIO MENSAL (ÚLTIMOS 6 MESES) ===");

        LocalDate hoje = LocalDate.now();


        for (int i = 5; i >= 0; i--) {
            LocalDate mesReferencia = hoje.minusMonths(i);
            int mes = mesReferencia.getMonthValue();
            int ano = mesReferencia.getYear();

            double totalMes = calcularTotalMes(mes, ano);
            int qtdPedidosMes = contarPedidosMes(mes, ano);


            String nomeMes = getNomeMesPortugues(mesReferencia.getMonth());

            System.out.printf("%s/%d: %d pedidos - R$%.2f%n",
                    nomeMes, ano, qtdPedidosMes, totalMes);
        }
    }

    private String getNomeMesPortugues(Month month) {
        String[] meses = {
                "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
        };
        return meses[month.getValue() - 1];
    }

    private double calcularTotalMes(int mes, int ano) {
        double total = 0;
        for (Pedido pedido : pedidos) {
            LocalDate dataPedido = pedido.getDataPedido();
            if (dataPedido.getMonthValue() == mes && dataPedido.getYear() == ano) {
                total += pedido.calcularTotal();
            }
        }
        return total;
    }

    private int contarPedidosMes(int mes, int ano) {
        int count = 0;
        for (Pedido pedido : pedidos) {
            LocalDate dataPedido = pedido.getDataPedido();
            if (dataPedido.getMonthValue() == mes && dataPedido.getYear() == ano) {
                count++;
            }
        }
        return count;
    }

    public void salvarDados() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("loja.dat"))) {
            oos.writeObject(this);
        }
    }

    public void carregarDados() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("loja.dat"))) {
            Loja lojaCarregada = (Loja) ois.readObject();
            this.pessoas = lojaCarregada.pessoas;
            this.produtos = lojaCarregada.produtos;
            this.pedidos = lojaCarregada.pedidos;
        }
    }
}