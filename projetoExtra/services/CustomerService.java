package projetoExtra.services;

import projetoExtra.database.DatabaseConnection;
import projetoExtra.entities.*;
import projetoExtra.exceptions.InvalidZipCodeException;
import projetoExtra.exceptions.InvalidCPFException;
import projetoExtra.exceptions.CustomerNotFoundException;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerService {

    private List<Customer> customers;
    private APIConsumer apiConsumer;

    public CustomerService() {
        this.customers = new ArrayList<>();
        this.apiConsumer = new APIConsumer();
        loadCustomersFromDatabase();
    }

    public void addCustomer(Customer customer) {
        if (customer != null && !customers.contains(customer)) {
            customers.add(customer);
            saveCustomerToDatabase(customer);
            System.out.println("✅ Cliente adicionado: " + customer.getName());
        }
    }

    public Customer findCustomerById(int id) throws CustomerNotFoundException {
        for (Customer customer : customers) {
            if (customer.getId() != null && customer.getId() == id) {
                return customer;
            }
        }

        Customer customerFromDb = loadCustomerFromDatabase(id);
        if (customerFromDb != null) {
            customers.add(customerFromDb);
            return customerFromDb;
        }

        throw new CustomerNotFoundException("Cliente com ID " + id + " não encontrado");
    }

    private Customer loadCustomerFromDatabase(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer;

                    if ("FISICA".equals(rs.getString("tipo"))) {
                        customer = new IndividualCustomer();
                        ((IndividualCustomer) customer).setCpf(rs.getString("cpf_cnpj"));
                    } else {
                        customer = new BusinessCustomer();
                        try {
                            ((BusinessCustomer) customer).setCnpj(rs.getString("cpf_cnpj"));
                        } catch (Exception e) {
                            System.out.println("Erro ao carregar CNPJ: " + e.getMessage());
                        }
                    }

                    customer.setId(rs.getInt("id"));
                    customer.setName(rs.getString("nome"));
                    customer.setEmail(rs.getString("email"));
                    customer.setPhone(rs.getString("telefone"));

                    loadAddressesFromDatabase(customer);
                    loadCategoriesFromDatabase(customer);

                    return customer;
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao carregar cliente do banco: " + e.getMessage());
        } catch (InvalidCPFException e) {
            System.out.println("❌ Erro ao carregar CPF: " + e.getMessage());
        }

        return null;
    }

    public void listCustomers() {
        System.out.println("\n=== LISTA DE CLIENTES ===");
        if (customers.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        for (int i = 0; i < customers.size(); i++) {
            Customer customer = customers.get(i);
            System.out.println((i + 1) + ". " + customer.toString());

            if (!customer.getAddresses().isEmpty()) {
                System.out.println("   Endereços:");
                for (Address address : customer.getAddresses()) {
                    System.out.println("   - " + address.toString());
                }
            }

            if (!customer.getCategories().isEmpty()) {
                System.out.println("   Categorias: " +
                        customer.getCategories().stream()
                                .map(Category::getName)
                                .reduce((a, b) -> a + ", " + b)
                                .orElse(""));
            }
            System.out.println();
        }
    }

    public List<Customer> searchCustomersByName(String name) {
        List<Customer> results = new ArrayList<>();

        for (Customer customer : customers) {
            if (customer.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(customer);
            }
        }
        return results;
    }

    public void addAddressByZipCode(int customerId, String zipCode, String number, String complement)
            throws CustomerNotFoundException, InvalidZipCodeException {

        Customer customer = findCustomerById(customerId);
        Address address = apiConsumer.consultarCEP(zipCode);

        address.setNumber(number);
        address.setComplement(complement);

        customer.addAddress(address);
        saveAddressToDatabase(address, customerId);

        System.out.println("✅ Endereço adicionado: " + address.toString());
    }

    public void deleteCustomer(int customerId) throws CustomerNotFoundException {
        Customer customerToDelete = findCustomerById(customerId);

        if (customerToDelete != null) {
            deleteCustomerFromDatabase(customerId);
            customers.remove(customerToDelete);
            System.out.println("✅ Cliente removido com sucesso: " + customerToDelete.getName());
        }
    }

    private void deleteCustomerFromDatabase(int customerId) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("⚠️  Nenhum cliente encontrado com ID: " + customerId);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao remover cliente do banco: " + e.getMessage());
        }
    }

    public void deleteCustomerByName(String name) {
        List<Customer> customersToDelete = searchCustomersByName(name);

        if (customersToDelete.isEmpty()) {
            System.out.println("🔍 Nenhum cliente encontrado com o nome: " + name);
            return;
        }

        if (customersToDelete.size() == 1) {
            try {
                deleteCustomer(customersToDelete.get(0).getId());
            } catch (CustomerNotFoundException e) {
                System.out.println("❌ " + e.getMessage());
            }
        } else {
            System.out.println("\n=== MÚLTIPLOS CLIENTES ENCONTRADOS ===");
            for (int i = 0; i < customersToDelete.size(); i++) {
                Customer customer = customersToDelete.get(i);
                System.out.println((i + 1) + ". " + customer.toString());
            }

            System.out.print("\nDigite o número do cliente que deseja remover (0 para cancelar): ");
            try {
                Scanner scanner = new Scanner(System.in);
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice > 0 && choice <= customersToDelete.size()) {
                    deleteCustomer(customersToDelete.get(choice - 1).getId());
                } else if (choice != 0) {
                    System.out.println("❌ Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Por favor, digite um número válido.");
            } catch (CustomerNotFoundException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    public void makeBackup(String fileName) {
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(fileName))) {

            output.writeObject(customers);
            System.out.println("✅ Backup realizado com sucesso: " + fileName);

        } catch (IOException e) {
            System.out.println("❌ Erro ao fazer backup: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void restoreBackup(String fileName) {
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(fileName))) {

            customers = (List<Customer>) input.readObject();
            System.out.println("✅ Backup restaurado com sucesso: " + fileName);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Erro ao restaurar backup: " + e.getMessage());
        }
    }

    private void loadCustomersFromDatabase() {
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer customer;

                if ("FISICA".equals(rs.getString("tipo"))) {
                    customer = new IndividualCustomer();
                    ((IndividualCustomer) customer).setCpf(rs.getString("cpf_cnpj"));
                } else {
                    customer = new BusinessCustomer();
                    try {
                        ((BusinessCustomer) customer).setCnpj(rs.getString("cpf_cnpj"));
                    } catch (Exception e) {
                        System.out.println("Erro ao carregar CNPJ: " + e.getMessage());
                    }
                }

                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("nome"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("telefone"));

                loadAddressesFromDatabase(customer);
                loadCategoriesFromDatabase(customer);

                customers.add(customer);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao carregar clientes do banco: " + e.getMessage());
        } catch (InvalidCPFException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveCustomerToDatabase(Customer customer) {
        String sql = "INSERT INTO clientes (nome, email, telefone, tipo, cpf_cnpj) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());

            if (customer instanceof IndividualCustomer) {
                stmt.setString(4, "FISICA");
                stmt.setString(5, ((IndividualCustomer) customer).getCpf());
            } else {
                stmt.setString(4, "JURIDICA");
                stmt.setString(5, ((BusinessCustomer) customer).getCnpj());
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    customer.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar cliente no banco: " + e.getMessage());
        }
    }

    private void loadAddressesFromDatabase(Customer customer) {
        String sql = "SELECT * FROM enderecos WHERE cliente_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customer.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Address address = new Address();
                    address.setId(rs.getInt("id"));
                    address.setZipCode(rs.getString("cep"));
                    address.setStreet(rs.getString("logradouro"));
                    address.setNumber(rs.getString("numero"));
                    address.setComplement(rs.getString("complemento"));
                    address.setNeighborhood(rs.getString("bairro"));
                    address.setCity(rs.getString("cidade"));
                    address.setState(rs.getString("estado"));
                    address.setType(rs.getString("tipo"));

                    customer.addAddress(address);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao carregar endereços: " + e.getMessage());
        }
    }

    private void saveAddressToDatabase(Address address, int customerId) {
        String sql = "INSERT INTO enderecos (cliente_id, cep, logradouro, numero, complemento, bairro, cidade, estado, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, customerId);
            stmt.setString(2, address.getZipCode());
            stmt.setString(3, address.getStreet());
            stmt.setString(4, address.getNumber());
            stmt.setString(5, address.getComplement());
            stmt.setString(6, address.getNeighborhood());
            stmt.setString(7, address.getCity());
            stmt.setString(8, address.getState());
            stmt.setString(9, address.getType() != null ? address.getType() : "RESIDENCIAL");

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    address.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar endereço no banco: " + e.getMessage());
        }
    }

    private void loadCategoriesFromDatabase(Customer customer) {
        String sql = "SELECT c.* FROM categorias c " +
                "JOIN cliente_categoria cc ON c.id = cc.categoria_id " +
                "WHERE cc.cliente_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customer.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("id"));
                    category.setName(rs.getString("nome"));
                    category.setDescription(rs.getString("descricao"));

                    customer.addCategory(category);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao carregar categorias: " + e.getMessage());
        }
    }


}