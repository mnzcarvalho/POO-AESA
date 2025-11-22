package pooP2;

import pooP2.exception.CPFInvalidoException;
import pooP2.exception.ClienteNaoEncontradoException;
import pooP2.exception.EstoqueInsuficienteException;
import pooP2.exception.ProdutoNaoEncontradoException;
import pooP2.model.Cliente;
import pooP2.model.Loja;
import pooP2.model.Pedido;
import pooP2.model.Produto;

import java.util.*;
import java.io.*;

public class Main {
    private static Loja loja = new Loja();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        carregarDados();
        exibirMenu();
    }

    private static void exibirMenu() {
        int opcao;
        do {
            System.out.println("\n=== LOJA MINIMALISTA ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Cadastrar Produto");
            System.out.println("3. Realizar Pedido");
            System.out.println("4. Listar Clientes");
            System.out.println("5. Listar Produtos");
            System.out.println("6. Listar Pedidos");
            System.out.println("7. Relatório de Vendas");
            System.out.println("8. Relatório Mensal (6 meses)"); // NOVA OPÇÃO
            System.out.println("9. Gerar Dados Fictícios"); // NOVA OPÇÃO
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch(opcao) {
                case 1 -> cadastrarCliente();
                case 2 -> cadastrarProduto();
                case 3 -> realizarPedido();
                case 4 -> listarClientes();
                case 5 -> listarProdutos();
                case 6 -> listarPedidos();
                case 7 -> gerarRelatorio();
                case 8 -> gerarRelatorioMensal(); // NOVO
                case 9 -> gerarDadosFicticios(); // NOVO
                case 0 -> salvarDados();
            }
        } while(opcao != 0);
    }
    
    private static void cadastrarCliente() {
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            Cliente cliente = new Cliente(nome, cpf, email);
            loja.adicionarCliente(cliente);
            System.out.println("Cliente cadastrado!");
        } catch (CPFInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private static void cadastrarProduto() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Preço: ");
        double preco = scanner.nextDouble();
        System.out.print("Estoque: ");
        int estoque = scanner.nextInt();
        scanner.nextLine();
        
        Produto produto = new Produto(nome, preco, estoque);
        loja.adicionarProduto(produto);
        System.out.println("Produto cadastrado!");
    }
    
    private static void realizarPedido() {
        try {
            System.out.print("CPF do Cliente: ");
            String cpf = scanner.nextLine();
            Cliente cliente = loja.buscarCliente(cpf);
            
            Pedido pedido = new Pedido(cliente);

            while(true) {
                System.out.print("ID do Produto (0 para finalizar): ");
                int id = scanner.nextInt();
                if(id == 0) break;
                
                System.out.print("Quantidade: ");
                int quantidade = scanner.nextInt();
                
                Produto produto = loja.buscarProduto(id);
                pedido.adicionarItem(produto, quantidade);
            }

            loja.adicionarPedido(pedido);
            System.out.println("Pedido realizado! Total: " + String.format("R$ %.2f", pedido.calcularTotal()));
            
        } catch (ClienteNaoEncontradoException | ProdutoNaoEncontradoException |
                 EstoqueInsuficienteException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private static void listarClientes() {
        loja.listarClientes();
    }
    
    private static void listarProdutos() {
        loja.listarProdutos();
    }
    
    private static void listarPedidos() {
        loja.listarPedidos();
    }
    
    private static void gerarRelatorio() {
        loja.gerarRelatorioVendas();
    }
    
    private static void salvarDados() {
        try {
            loja.salvarDados();
            System.out.println("Dados salvos!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }
    
    private static void carregarDados() {
        try {
            loja.carregarDados();
            System.out.println("Dados carregados!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Nenhum dado anterior encontrado");
        }
    }

    private static void gerarRelatorioMensal() {
        loja.gerarRelatorioMensal();
    }

    private static void gerarDadosFicticios() {
        loja.gerarDadosFicticios();
    }
}
