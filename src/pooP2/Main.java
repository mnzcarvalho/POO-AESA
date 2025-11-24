package pooP2;

import pooP2.exception.CPFInvalidoException;
import pooP2.exception.ClienteNaoEncontradoException;
import pooP2.exception.EstoqueInsuficienteException;
import pooP2.exception.ProdutoNaoEncontradoException;
import pooP2.model.Cliente;
import pooP2.model.Loja;
import pooP2.model.Pedido;
import pooP2.model.Produto;
import pooP2.util.ImportadorCSV;

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
            System.out.println("\n=== LOJA TECH - SISTEMA DE GERENCIAMENTO ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Cadastrar Produto");
            System.out.println("3. Realizar Pedido");
            System.out.println("4. Listar Clientes");
            System.out.println("5. Listar Produtos");
            System.out.println("6. Listar Pedidos");
            System.out.println("7. Relatório de Vendas");
            System.out.println("8. Relatório Mensal (6 meses)");
            System.out.println("9. Gerar Dados Fictícios");
            System.out.println("10. Importar Produtos de CSV");      // NOVA OPÇÃO
            System.out.println("11. Salvar Relatório em Arquivo");   // NOVA OPÇÃO
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
                case 8 -> gerarRelatorioMensal();
                case 9 -> gerarDadosFicticios();
                case 10 -> importarProdutosCSV();    // NOVO
                case 11 -> salvarRelatorioArquivo(); // NOVO
                case 0 -> salvarDados();
                default -> System.out.println("Opção inválida!");
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


    private static void salvarRelatorioArquivo() {
        System.out.println("\n=== SALVAR RELATÓRIO EM ARQUIVO ===");
        System.out.print("Digite o nome do arquivo para salvar (ex: relatorio.txt): ");
        String arquivo = scanner.nextLine();

        try {
            loja.salvarRelatorioMensal(arquivo);
            System.out.println("Relatório salvo com sucesso em: " + arquivo);


            System.out.println("\nPreview do arquivo gerado:");
            try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    System.out.println(linha);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar relatório: " + e.getMessage());
        }
    }


    private static void criarExemploCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("produtos.csv"))) {

            pw.println("Nome;Preço;Estoque");

            pw.println("Notebook Gamer;4500.00;15");
            pw.println("Mouse Wireless;150.00;50");
            pw.println("Teclado Mecânico;350.00;30");
            pw.println("Monitor 27pol;1200.00;20");
            pw.println("Webcam Full HD;299.99;40");
            pw.println("Headphone Bluetooth;599.99;25");
            pw.println("Tablet Android;899.99;18");
            pw.println("Impressora Laser;699.99;12");

            System.out.println("✅ Arquivo 'produtos.csv' criado com sucesso!");
            System.out.println("📋 Conteúdo do arquivo:");
            System.out.println("Nome;Preço;Estoque");
            System.out.println("Notebook Gamer;4500.00;15");
            System.out.println("Mouse Wireless;150.00;50");
            System.out.println("... e mais produtos");

        } catch (IOException e) {
            System.out.println("❌ Erro ao criar arquivo exemplo: " + e.getMessage());
        }
    }

    private static void importarProdutosCSV() {
        System.out.println("\n=== IMPORTAR PRODUTOS DE ARQUIVO CSV ===");
        System.out.println("Formato esperado: Nome;Preço;Estoque");
        System.out.println("Exemplo: Notebook Gamer;4500.00;15");
        System.out.print("Digite o nome do arquivo CSV (ex: produtos.csv): ");
        String arquivo = scanner.nextLine();

        File file = new File(arquivo);
        if (!file.exists()) {
            System.out.println("❌ Arquivo não encontrado: " + arquivo);
            System.out.println("📁 Criando arquivo de exemplo 'produtos.csv'...");
            criarExemploCSV();
            System.out.println("✅ Arquivo de exemplo criado!");
            System.out.println("✏️  Edite o arquivo 'produtos.csv' e importe novamente.");
            return;
        }

        System.out.println("📂 Importando produtos de: " + arquivo);
        List<Produto> produtosImportados = ImportadorCSV.importarProdutosCSV(arquivo);

        if (!produtosImportados.isEmpty()) {
            for (Produto produto : produtosImportados) {
                try {
                    loja.adicionarProduto(produto);
                } catch (Exception e) {
                    System.out.println("⚠️  Erro ao adicionar produto: " + e.getMessage());
                }
            }
            System.out.println("✅ " + produtosImportados.size() + " produtos importados com sucesso!");
            System.out.println("📋 Use a opção 5 para listar todos os produtos.");
        } else {
            System.out.println("❌ Nenhum produto foi importado.");
            System.out.println("📝 Verifique o formato do arquivo CSV.");
        }
    }
}
