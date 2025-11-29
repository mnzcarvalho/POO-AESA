package projetoExtra.database;

import projetoExtra.entities.Cliente;
import projetoExtra.entities.ClientePessoaFisica;
import projetoExtra.entities.ClientePessoaJuridica;
import projetoExtra.exceptions.CEPInvalidoException;
import projetoExtra.exceptions.CPFInvalidoException;
import projetoExtra.exceptions.ClienteNaoEncontradoException;
import projetoExtra.services.ClienteService;



import java.util.List;
import java.util.Scanner;

import static projetoExtra.database.DatabaseConnection.*;

public class Main {
    private static ClienteService clienteService = new ClienteService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("  SISTEMA DE CADASTRO DE CLIENTES  ");
        System.out.println("====================================\n");

        // Testar conexão com banco de dados
        testConnection();

        // Menu principal
        exibirMenuPrincipal();
    }

    private static void exibirMenuPrincipal() {
        int opcao;

        // Estruturas lógicas: do-while para menu principal
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente por Nome");
            System.out.println("4. Adicionar Endereço por CEP");
            System.out.println("5. Fazer Backup dos Dados");
            System.out.println("6. Restaurar Backup");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                // Estruturas lógicas: switch para opções do menu
                switch (opcao) {
                    case 1:
                        cadastrarCliente();
                        break;
                    case 2:
                        clienteService.listarClientes();
                        break;
                    case 3:
                        buscarClientePorNome();
                        break;
                    case 4:
                        adicionarEnderecoPorCEP();
                        break;
                    case 5:
                        fazerBackup();
                        break;
                    case 6:
                        restaurarBackup();
                        break;
                    case 0:
                        System.out.println("👋 Obrigado por usar o sistema! Até logo!");
                        break;
                    default:
                        System.out.println("❌ Opção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, digite um número válido.");
                opcao = -1;
            } catch (Exception e) {
                System.out.println("❌ Erro: " + e.getMessage());
                opcao = -1;
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static void cadastrarCliente() {
        System.out.println("\n=== CADASTRAR CLIENTE ===");

        // Estruturas lógicas: switch para tipo de cliente
        System.out.println("Tipo de Cliente:");
        System.out.println("1. Pessoa Física");
        System.out.println("2. Pessoa Jurídica");
        System.out.print("Escolha: ");

        try {
            int tipo = Integer.parseInt(scanner.nextLine());

            // Coletar dados comuns
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            Cliente cliente;

            switch (tipo) {
                case 1:
                    // Cadastro Pessoa Física
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();

                    cliente = new ClientePessoaFisica(nome, email, telefone, cpf);

                    // Validar CPF
                    try {
                        ((ClientePessoaFisica) cliente).validarDocumento();
                    } catch (CPFInvalidoException e) {
                        System.out.println("❌ Erro no CPF: " + e.getMessage());
                        return;
                    }
                    break;

                case 2:
                    // Cadastro Pessoa Jurídica
                    System.out.print("CNPJ: ");
                    String cnpj = scanner.nextLine();

                    System.out.print("Razão Social: ");
                    String razaoSocial = scanner.nextLine();

                    cliente = new ClientePessoaJuridica(nome, email, telefone, cnpj, razaoSocial);

                    // Validar CNPJ
                    try {
                        ((ClientePessoaJuridica) cliente).validarDocumento();
                    } catch (Exception e) {
                        System.out.println("❌ Erro no CNPJ: " + e.getMessage());
                        return;
                    }
                    break;

                default:
                    System.out.println("❌ Tipo de cliente inválido!");
                    return;
            }

            // Adicionar cliente ao sistema
            clienteService.adicionarCliente(cliente);

            // Perguntar se deseja adicionar endereço
            System.out.print("\nDeseja adicionar um endereço? (s/n): ");
            String resposta = scanner.nextLine();

            if (resposta.equalsIgnoreCase("s")) {
                adicionarEnderecoCliente(cliente.getId());
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor, digite um número válido.");
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    private static void adicionarEnderecoCliente(int clienteId) {
        System.out.println("\n=== ADICIONAR ENDEREÇO ===");

        try {
            System.out.print("CEP: ");
            String cep = scanner.nextLine();

            System.out.print("Número: ");
            String numero = scanner.nextLine();

            System.out.print("Complemento: ");
            String complemento = scanner.nextLine();

            clienteService.adicionarEnderecoPorCEP(clienteId, cep, numero, complemento);

        } catch (ClienteNaoEncontradoException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (CEPInvalidoException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erro ao adicionar endereço: " + e.getMessage());
        }
    }

    private static void adicionarEnderecoPorCEP() {
        System.out.println("\n=== ADICIONAR ENDEREÇO POR CEP ===");

        try {
            clienteService.listarClientes();

            System.out.print("ID do Cliente: ");
            int clienteId = Integer.parseInt(scanner.nextLine());

            adicionarEnderecoCliente(clienteId);

        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor, digite um ID válido.");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private static void buscarClientePorNome() {
        System.out.println("\n=== BUSCAR CLIENTE POR NOME ===");

        System.out.print("Digite o nome (ou parte dele): ");
        String nome = scanner.nextLine();

        // Estruturas lógicas: if para verificar se o nome não está vazio
        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("❌ Por favor, digite um nome para buscar.");
            return;
        }

        List<Cliente> resultados = clienteService.buscarClientesPorNome(nome);

        // Estruturas lógicas: if-else para mostrar resultados
        if (resultados.isEmpty()) {
            System.out.println("🔍 Nenhum cliente encontrado com o nome: " + nome);
        } else {
            System.out.println("\n=== RESULTADOS DA BUSCA ===");
            for (int i = 0; i < resultados.size(); i++) {
                Cliente cliente = resultados.get(i);
                System.out.println((i + 1) + ". " + cliente.toString());
                System.out.println("   " + cliente.getInformacoesContato());

                // Mostrar primeiro endereço se existir
                if (!cliente.getEnderecos().isEmpty()) {
                    System.out.println("   Endereço: " + cliente.getEnderecos().get(0));
                }
                System.out.println();
            }
        }
    }

    private static void fazerBackup() {
        System.out.println("\n=== FAZER BACKUP ===");

        System.out.print("Nome do arquivo de backup (ex: backup_clientes.dat): ");
        String arquivo = scanner.nextLine();

        // Estruturas lógicas: if para validar nome do arquivo
        if (arquivo == null || arquivo.trim().isEmpty()) {
            arquivo = "backup_clientes.dat";
            System.out.println("Usando nome padrão: " + arquivo);
        }

        clienteService.fazerBackup(arquivo);
    }

    private static void restaurarBackup() {
        System.out.println("\n=== RESTAURAR BACKUP ===");

        System.out.println("⚠️  ATENÇÃO: Esta ação substituirá os dados atuais!");
        System.out.print("Deseja continuar? (s/n): ");
        String confirmacao = scanner.nextLine();

        // Estruturas lógicas: if para confirmação
        if (!confirmacao.equalsIgnoreCase("s")) {
            System.out.println("Operação cancelada.");
            return;
        }

        System.out.print("Nome do arquivo de backup: ");
        String arquivo = scanner.nextLine();

        if (arquivo == null || arquivo.trim().isEmpty()) {
            System.out.println("❌ Nome do arquivo inválido.");
            return;
        }

        clienteService.restaurarBackup(arquivo);
    }
}