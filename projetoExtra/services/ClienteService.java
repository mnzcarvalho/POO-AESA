package projetoExtra.services;


import projetoExtra.database.DatabaseConnection;
import projetoExtra.entities.*;
import projetoExtra.exceptions.CEPInvalidoException;
import projetoExtra.exceptions.CPFInvalidoException;
import projetoExtra.exceptions.ClienteNaoEncontradoException;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    private List<Cliente> clientes; // ArrayList polimórfico
    private APIConsumer apiConsumer;

    public ClienteService() {
        this.clientes = new ArrayList<>();
        this.apiConsumer = new APIConsumer();
        carregarClientesDoBanco();
    }

    public void adicionarCliente(Cliente cliente) {
        if (cliente != null && !clientes.contains(cliente)) {
            clientes.add(cliente);
            salvarClienteNoBanco(cliente);
            System.out.println("✅ Cliente adicionado: " + cliente.getNome());
        }
    }

    public Cliente buscarClientePorId(int id) throws ClienteNaoEncontradoException {
        for (Cliente cliente : clientes) {
            if (cliente.getId() != null && cliente.getId() == id) {
                return cliente;
            }
        }
        throw new ClienteNaoEncontradoException("Cliente com ID " + id + " não encontrado");
    }

    public void listarClientes() {
        System.out.println("\n=== LISTA DE CLIENTES ===");
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }

        for (int i = 0; i < clientes.size(); i++) {
            Cliente cliente = clientes.get(i);
            System.out.println((i + 1) + ". " + cliente.toString());

            if (!cliente.getEnderecos().isEmpty()) {
                System.out.println("   Endereços:");
                for (Endereco endereco : cliente.getEnderecos()) {
                    System.out.println("   - " + endereco.toString());
                }
            }

            if (!cliente.getCategorias().isEmpty()) {
                System.out.println("   Categorias: " +
                        cliente.getCategorias().stream()
                                .map(Categoria::getNome)
                                .reduce((a, b) -> a + ", " + b)
                                .orElse(""));
            }
            System.out.println();
        }
    }

    public List<Cliente> buscarClientesPorNome(String nome) {
        List<Cliente> resultados = new ArrayList<>();
        int i = 0;

        // Estruturas lógicas: loop while
        while (i < clientes.size()) {
            Cliente cliente = clientes.get(i);
            if (cliente.getNome().toLowerCase().contains(nome.toLowerCase())) {
                resultados.add(cliente);
            }
            i++;
        }
        return resultados;
    }

    public void adicionarEnderecoPorCEP(int clienteId, String cep, String numero, String complemento)
            throws ClienteNaoEncontradoException, CEPInvalidoException {

        Cliente cliente = buscarClientePorId(clienteId);
        Endereco endereco = apiConsumer.consultarCEP(cep);

        endereco.setNumero(numero);
        endereco.setComplemento(complemento);

        cliente.adicionarEndereco(endereco);
        salvarEnderecoNoBanco(endereco, clienteId);

        System.out.println("✅ Endereço adicionado: " + endereco.toString());
    }

    public void fazerBackup(String arquivo) {
        try (ObjectOutputStream output = new ObjectOutputStream(
                new FileOutputStream(arquivo))) {

            output.writeObject(clientes);
            System.out.println("✅ Backup realizado com sucesso: " + arquivo);

        } catch (IOException e) {
            System.out.println("❌ Erro ao fazer backup: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void restaurarBackup(String arquivo) {
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(arquivo))) {

            clientes = (List<Cliente>) input.readObject();
            System.out.println("✅ Backup restaurado com sucesso: " + arquivo);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("❌ Erro ao restaurar backup: " + e.getMessage());
        }
    }


    private void carregarClientesDoBanco() {
        String sql = "SELECT * FROM clientes";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente;

                if ("FISICA".equals(rs.getString("tipo"))) {
                    cliente = new ClientePessoaFisica();
                    ((ClientePessoaFisica) cliente).setCpf(rs.getString("cpf_cnpj"));
                } else {
                    cliente = new ClientePessoaJuridica();
                    try {
                        ((ClientePessoaJuridica) cliente).setCnpj(rs.getString("cpf_cnpj"));
                    } catch (Exception e) {
                        System.out.println("Erro ao carregar CNPJ: " + e.getMessage());
                    }
                }

                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setEmail(rs.getString("email"));
                cliente.setTelefone(rs.getString("telefone"));

                carregarEnderecosDoBanco(cliente);
                carregarCategoriasDoBanco(cliente);

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao carregar clientes do banco: " + e.getMessage());
        } catch (CPFInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    private void salvarClienteNoBanco(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, email, telefone, tipo, cpf_cnpj) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefone());

            if (cliente instanceof ClientePessoaFisica) {
                stmt.setString(4, "FISICA");
                stmt.setString(5, ((ClientePessoaFisica) cliente).getCpf());
            } else {
                stmt.setString(4, "JURIDICA");
                stmt.setString(5, ((ClientePessoaJuridica) cliente).getCnpj());
            }

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cliente.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar cliente no banco: " + e.getMessage());
        }
    }

    private void carregarEnderecosDoBanco(Cliente cliente) {
        String sql = "SELECT * FROM enderecos WHERE cliente_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cliente.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Endereco endereco = new Endereco();
                    endereco.setId(rs.getInt("id"));
                    endereco.setCep(rs.getString("cep"));
                    endereco.setLogradouro(rs.getString("logradouro"));
                    endereco.setNumero(rs.getString("numero"));
                    endereco.setComplemento(rs.getString("complemento"));
                    endereco.setBairro(rs.getString("bairro"));
                    endereco.setCidade(rs.getString("cidade"));
                    endereco.setEstado(rs.getString("estado"));
                    endereco.setTipo(rs.getString("tipo"));

                    cliente.adicionarEndereco(endereco);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao carregar endereços: " + e.getMessage());
        }
    }

    private void salvarEnderecoNoBanco(Endereco endereco, int clienteId) {
        String sql = "INSERT INTO enderecos (cliente_id, cep, logradouro, numero, complemento, bairro, cidade, estado, tipo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, clienteId);
            stmt.setString(2, endereco.getCep());
            stmt.setString(3, endereco.getLogradouro());
            stmt.setString(4, endereco.getNumero());
            stmt.setString(5, endereco.getComplemento());
            stmt.setString(6, endereco.getBairro());
            stmt.setString(7, endereco.getCidade());
            stmt.setString(8, endereco.getEstado());
            stmt.setString(9, endereco.getTipo() != null ? endereco.getTipo() : "RESIDENCIAL");

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    endereco.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar endereço no banco: " + e.getMessage());
        }
    }

    private void carregarCategoriasDoBanco(Cliente cliente) {
        String sql = "SELECT c.* FROM categorias c " +
                "JOIN cliente_categoria cc ON c.id = cc.categoria_id " +
                "WHERE cc.cliente_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cliente.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getInt("id"));
                    categoria.setNome(rs.getString("nome"));
                    categoria.setDescricao(rs.getString("descricao"));

                    cliente.adicionarCategoria(categoria);
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Erro ao carregar categorias: " + e.getMessage());
        }
    }
}