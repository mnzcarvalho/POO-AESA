package projetoExtra.database;

import projetoExtra.entities.*;
import projetoExtra.exceptions.InvalidZipCodeException;
import projetoExtra.exceptions.InvalidCPFException;
import projetoExtra.exceptions.CustomerNotFoundException;
import projetoExtra.services.CustomerService;

import java.util.List;
import java.util.Scanner;

import static projetoExtra.database.DatabaseConnection.*;

public class Main {
    private static CustomerService customerService = new CustomerService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("  SISTEMA DE CADASTRO DE CLIENTES  ");
        System.out.println("====================================\n");

        testConnection();
        showMainMenu();
    }

    private static void showMainMenu() {
        int option;
        do {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Buscar Cliente por Nome");
            System.out.println("4. Remover Cliente");
            System.out.println("5. Adicionar Endereço por CEP");
            System.out.println("6. Fazer Backup dos Dados");
            System.out.println("7. Restaurar Backup");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
                switch (option) {
                    case 1:
                        registerCustomer();
                        break;
                    case 2:
                        customerService.listCustomers();
                        break;
                    case 3:
                        searchCustomerByName();
                        break;
                    case 4:
                        deleteCustomer();
                        break;
                    case 5:
                        addAddressByZipCode();
                        break;
                    case 6:
                        makeBackup();
                        break;
                    case 7:
                        restoreBackup();
                        break;
                    case 0:
                        System.out.println("👋 Obrigado por usar o sistema! Até logo!");
                        break;
                    default:
                        System.out.println("❌ Opção inválida! Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, digite um número válido.");
                option = -1;
            }
        } while (option != 0);
        scanner.close();
    }

    private static void deleteCustomer() {
        System.out.println("\n=== REMOVER CLIENTE ===");
        System.out.println("1. Remover por ID");
        System.out.println("2. Remover por Nome");
        System.out.println("3. Voltar");
        System.out.print("Escolha uma opção: ");

        try {
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    deleteCustomerById();
                    break;
                case 2:
                    deleteCustomerByName();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("❌ Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor, digite um número válido.");
        }
    }

    private static void deleteCustomerById() {
        try {
            customerService.listCustomers();
            System.out.print("\nDigite o ID do cliente que deseja remover: ");
            int customerId = Integer.parseInt(scanner.nextLine());

            System.out.print("⚠️  Tem certeza que deseja remover este cliente? (s/n): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("s")) {
                customerService.deleteCustomer(customerId);
            } else {
                System.out.println("Operação cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor, digite um ID válido.");
        } catch (CustomerNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    private static void deleteCustomerByName() {
        System.out.print("Digite o nome do cliente que deseja remover: ");
        String name = scanner.nextLine();

        if (name == null || name.trim().isEmpty()) {
            System.out.println("❌ Por favor, digite um nome para buscar.");
            return;
        }

        customerService.deleteCustomerByName(name);
    }


    private static void registerCustomer() {
        System.out.println("\n=== CADASTRAR CLIENTE ===");
        System.out.println("Tipo de Cliente:");
        System.out.println("1. Pessoa Física");
        System.out.println("2. Pessoa Jurídica");
        System.out.print("Escolha: ");

        try {
            int tipo = Integer.parseInt(scanner.nextLine());

            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            Customer customer;

            switch (tipo) {
                case 1:
                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();
                    customer = new IndividualCustomer(nome, email, telefone, cpf);
                    try {
                        ((IndividualCustomer) customer).validateDocument();
                    } catch (InvalidCPFException e) {
                        System.out.println("❌ Erro no CPF: " + e.getMessage());
                        return;
                    }
                    break;
                case 2:
                    System.out.print("CNPJ: ");
                    String cnpj = scanner.nextLine();
                    System.out.print("Razão Social: ");
                    String razaoSocial = scanner.nextLine();
                    customer = new BusinessCustomer(nome, email, telefone, cnpj, razaoSocial);
                    try {
                        ((BusinessCustomer) customer).validateDocument();
                    } catch (Exception e) {
                        System.out.println("❌ Erro no CNPJ: " + e.getMessage());
                        return;
                    }
                    break;
                default:
                    System.out.println("❌ Tipo de cliente inválido!");
                    return;
            }

            customerService.addCustomer(customer);
            System.out.print("\nDeseja adicionar um endereço? (s/n): ");
            String resposta = scanner.nextLine();
            if (resposta.equalsIgnoreCase("s")) {
                addCustomerAddress(customer.getId());
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor, digite um número válido.");
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    private static void addCustomerAddress(int customerId) {
        System.out.println("\n=== ADICIONAR ENDEREÇO ===");
        try {
            System.out.print("CEP: ");
            String cep = scanner.nextLine();
            System.out.print("Número: ");
            String numero = scanner.nextLine();
            System.out.print("Complemento: ");
            String complemento = scanner.nextLine();
            customerService.addAddressByZipCode(customerId, cep, numero, complemento);
        } catch (CustomerNotFoundException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (InvalidZipCodeException e) {
            System.out.println("❌ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erro ao adicionar endereço: " + e.getMessage());
        }
    }

    private static void addAddressByZipCode() {
        System.out.println("\n=== ADICIONAR ENDEREÇO POR CEP ===");
        try {
            customerService.listCustomers();
            System.out.print("ID do Cliente: ");
            int customerId = Integer.parseInt(scanner.nextLine());
            addCustomerAddress(customerId);
        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor, digite um ID válido.");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private static void searchCustomerByName() {
        System.out.println("\n=== BUSCAR CLIENTE POR NOME ===");
        System.out.print("Digite o nome (ou parte dele): ");
        String nome = scanner.nextLine();

        if (nome == null || nome.trim().isEmpty()) {
            System.out.println("❌ Por favor, digite um nome para buscar.");
            return;
        }

        List<Customer> resultados = customerService.searchCustomersByName(nome);

        if (resultados.isEmpty()) {
            System.out.println("🔍 Nenhum cliente encontrado com o nome: " + nome);
        } else {
            System.out.println("\n=== RESULTADOS DA BUSCA ===");
            for (int i = 0; i < resultados.size(); i++) {
                Customer customer = resultados.get(i);
                System.out.println((i + 1) + ". " + customer.toString());
                System.out.println("   " + customer.getContactInfo());

                if (!customer.getAddresses().isEmpty()) {
                    System.out.println("   Endereço: " + customer.getAddresses().get(0));
                }
                System.out.println();
            }
        }
    }

    private static void makeBackup() {
        System.out.println("\n=== FAZER BACKUP ===");
        System.out.print("Nome do arquivo de backup (ex: backup_clientes.dat): ");
        String arquivo = scanner.nextLine();

        if (arquivo == null || arquivo.trim().isEmpty()) {
            arquivo = "backup_clientes.dat";
            System.out.println("Usando nome padrão: " + arquivo);
        }
        customerService.makeBackup(arquivo);
    }

    private static void restoreBackup() {
        System.out.println("\n=== RESTAURAR BACKUP ===");
        System.out.println("⚠️  ATENÇÃO: Esta ação substituirá os dados atuais!");
        System.out.print("Deseja continuar? (s/n): ");
        String confirmacao = scanner.nextLine();

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
        customerService.restoreBackup(arquivo);
    }
}