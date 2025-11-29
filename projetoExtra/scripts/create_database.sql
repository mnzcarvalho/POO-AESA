CREATE DATABASE IF NOT EXISTS sistema_clientes;
USE sistema_clientes;

CREATE TABLE IF NOT EXISTS categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT
);


CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    email VARCHAR(150),
    telefone VARCHAR(20),
    tipo ENUM('FISICA', 'JURIDICA') NOT NULL,
    cpf_cnpj VARCHAR(20) UNIQUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS enderecos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    cep VARCHAR(10) NOT NULL,
    logradouro VARCHAR(200),
    numero VARCHAR(10),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    tipo ENUM('RESIDENCIAL', 'COMERCIAL', 'ENTREGA') DEFAULT 'RESIDENCIAL',
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS cliente_categoria (
    cliente_id INT,
    categoria_id INT,
    data_associacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cliente_id, categoria_id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE CASCADE
);


INSERT INTO categorias (nome, descricao) VALUES 
('VIP', 'Clientes preferenciais'),
('Regular', 'Clientes comuns'),
('Novo', 'Clientes recentes'),
('Inativo', 'Clientes sem movimentação');


INSERT INTO clientes (nome, email, telefone, tipo, cpf_cnpj) VALUES 
('João Silva', 'joao@email.com', '(11) 9999-8888', 'FISICA', '123.456.789-00'),
('Empresa XYZ Ltda', 'contato@xyz.com', '(11) 3333-4444', 'JURIDICA', '12.345.678/0001-90');