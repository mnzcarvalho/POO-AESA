package projetoExtra.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_clientes";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            createTablesIfNotExist(conn);
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado", e);
        }
    }

    private static void createTablesIfNotExist(Connection conn) {
        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS clientes (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "nome VARCHAR(100) NOT NULL, " +
                            "email VARCHAR(100) UNIQUE NOT NULL, " +
                            "telefone VARCHAR(20), " +
                            "tipo ENUM('FISICA', 'JURIDICA') NOT NULL, " +
                            "cpf_cnpj VARCHAR(18), " +
                            "razao_social VARCHAR(100), " +
                            "data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
            );

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS enderecos (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "cliente_id INT, " +
                            "cep VARCHAR(9) NOT NULL, " +
                            "logradouro VARCHAR(100), " +
                            "numero VARCHAR(10), " +
                            "complemento VARCHAR(100), " +
                            "bairro VARCHAR(50), " +
                            "cidade VARCHAR(50), " +
                            "estado VARCHAR(2), " +
                            "tipo VARCHAR(20) DEFAULT 'RESIDENCIAL', " +
                            "FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE)"
            );

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS categorias (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "nome VARCHAR(50) NOT NULL UNIQUE, " +
                            "descricao VARCHAR(200))"
            );

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS cliente_categoria (" +
                            "cliente_id INT, " +
                            "categoria_id INT, " +
                            "data_associacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "PRIMARY KEY (cliente_id, categoria_id), " +
                            "FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE, " +
                            "FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE CASCADE)"
            );

            stmt.executeUpdate(
                    "INSERT IGNORE INTO categorias (nome, descricao) VALUES " +
                            "('VIP', 'Clientes preferenciais'), " +
                            "('Regular', 'Clientes comuns'), " +
                            "('Corporate', 'Clientes corporativos')"
            );

        } catch (SQLException e) {
            System.out.println("❌ ERRO AO CRIAR TABELAS: " + e.getMessage());
        }
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ CONEXÃO COM BANCO DE DADOS ESTABELECIDA!");
        } catch (SQLException e) {
            System.out.println("❌ ERRO NA CONEXÃO: " + e.getMessage());
        }
    }
}