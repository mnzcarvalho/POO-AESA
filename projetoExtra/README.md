# 🏢 Sistema de Cadastro de Clientes

## 📖 O que é?
Sistema completo de cadastro e gerenciamento de clientes desenvolvido em Java, com interface console e persistência em banco de dados MySQL. Permite cadastrar clientes (pessoas físicas e jurídicas) com consulta automática de endereço via API ViaCEP.

## 🎯 O que faz?
### Funcionalidades Principais
- ✅ **Cadastro de Clientes** - Pessoa Física (CPF) e Pessoa Jurídica (CNPJ)
- ✅ **Validação Automática** - CPF/CNPJ validados automaticamente
- ✅ **Consulta de Endereço** - Busca automática via CEP (API ViaCEP)
- ✅ **Gestão de Endereços** - Múltiplos endereços por cliente
- ✅ **Categorização** - Categorias VIP, Regular, Corporate, etc.
- ✅ **Busca Avançada** - Por nome, listagem completa
- ✅ **Backup e Restauração** - Exportação/importação de dados
- ✅ **Exclusão de Clientes** - Remoção por ID ou nome
- ✅ **Persistência** - Dados salvos em banco MySQL

## Características Técnicas
- **Arquitetura**: MVC com separação de concerns
- **Persistência**: MySQL com JDBC
- **APIs**: Integração com ViaCEP
- **Validações**: CPF, CNPJ, email, campos obrigatórios
- **Serialização**: Backup em arquivos .dat

## 🛠️ Tecnologias Utilizadas
- **Java 8+** - Linguagem principal
- **MySQL** - Banco de dados relacional
- **JDBC** - Conexão com banco de dados
- **API ViaCEP** - Consulta de endereços
- **Serialização Java** - Sistema de backup

## 📋 Pré-requisitos
- Java JDK 8 ou superior
- MySQL Server 5.7+ ou MySQL 8.0
- MySQL Workbench (recomendado)
- Conexão com internet (para API ViaCEP)

### Script SQL de Criação
Crie o arquivo `schema_clientes.sql` com o seguinte conteúdo:

```sql
-- =============================================
-- SISTEMA DE CADASTRO DE CLIENTES - BANCO DE DADOS
-- =============================================

-- Criar o banco de dados
CREATE DATABASE IF NOT EXISTS sistema_clientes 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Usar o banco de dados
USE sistema_clientes;

-- Tabela de clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    tipo ENUM('FISICA', 'JURIDICA') NOT NULL,
    cpf_cnpj VARCHAR(18),
    razao_social VARCHAR(100),
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de endereços
CREATE TABLE IF NOT EXISTS enderecos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    cep VARCHAR(9) NOT NULL,
    logradouro VARCHAR(100),
    numero VARCHAR(10),
    complemento VARCHAR(100),
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(2),
    tipo VARCHAR(20) DEFAULT 'RESIDENCIAL',
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE
);

-- Tabela de categorias
CREATE TABLE IF NOT EXISTS categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE,
    descricao VARCHAR(200)
);

-- Tabela de relação cliente-categoria
CREATE TABLE IF NOT EXISTS cliente_categoria (
    cliente_id INT,
    categoria_id INT,
    data_associacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (cliente_id, categoria_id),
    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE CASCADE
);

-- Dados iniciais
INSERT IGNORE INTO categorias (nome, descricao) VALUES 
('VIP', 'Clientes preferenciais'),
('Regular', 'Clientes comuns'),
('Corporate', 'Clientes corporativos');
```
## 🔌 Como Configurar a Conexão com o Banco
1. Criar o Banco de Dados Via MySQL Workbench:

1. Abra o MySQL Workbench

2. Conecte-se ao servidor local (localhost)

3. Execute apenas: CREATE DATABASE sistema_clientes;

 Ou via Linha de Comando:
```
bash

mysql -u root -p -e "CREATE DATABASE sistema_clientes;"
```


2. Configurar Credenciais no Código                 
Edite o arquivo DatabaseConnection.java:
```
java
public class DatabaseConnection {
private static final String URL = "jdbc:mysql://localhost:3306/sistema_clientes";
private static final String USER = "root";
private static final String PASSWORD = "sua_senha_aqui"; // ALTERE AQUI

```
3. Configurações de Conexão
   Host: localhost

   Porta: 3306 (padrão MySQL)

   Banco: sistema_clientes

   Usuário: root (ou outro usuário com privilégios)

   Senha: [sua senha do MySQL]

## 🔧 Solução de Problemas de Conexão       
Erro de acesso negado:
```
sql
-- Conceder privilégios (se necessário)
GRANT ALL PRIVILEGES ON sistema_clientes.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

Driver não encontrado:              

Baixe o connector MySQL: https://dev.mysql.com/downloads/connector/j/           

Adicione o JAR ao classpath do projeto              

## 🚀 Como Instalar e Executar                   
Passo a Passo Completo:  **Intellij e Terminal**        
Terminal:
```
bash
# Verificar Java
java -version

# Verificar MySQL
mysql --version
```
### Configurar Banco de Dados           

Execute o script SQL no MySQL Workbench             

Ou importe via linha de comando:            
```
bash
mysql -u root -p < schema_clientes.sql
```

### Configurar Conexão

Edite DatabaseConnection.java com suas credenciais

Verifique a senha do MySQL

Compilar e Executar

```
bash
# Compilar (dependendo da sua IDE)
javac -cp ".;mysql-connector-java-8.0.x.jar" *.java

# Executar
java -cp ".;mysql-connector-java-8.0.x.jar" projetoExtra.database.Main
```

### Usar o Sistema

 Siga o menu interativo

 As tabelas são criadas automaticamente na primeira execução

### Problemas com CEP

Teste com:
```
text
=== ADICIONAR ENDEREÇO ===
CEP: 01414001
Número: 1000
Complemento: Apartamento 10
```
## 💡 Executando no IntelliJ IDEA

### Clonar e Configurar o Projeto

1. **Clonar o Repositório**
   - Abra o IntelliJ IDEA
   - Na tela inicial, selecione **"Get from VCS"**
   - Cole a URL SSH: `git@github.com:mnzcarvalho/POO-AESA.git`
   - Escolha o diretório local e clique em **Clone**
   - Se pedir credenciais, use sua chave SSH do GitHub

2. **Configurar o JDK**
   - **File** → **Project Structure** (Ctrl+Alt+Shift+S)
   - Em **Project Settings** → **Project**
   - **Project SDK**: Selecione JDK 8 ou superior
   - **Project language level**: 8 ou superior

3. **Configurar MySQL Connector**
   - Baixe o [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
   - **Platform Independent** → Baixar arquivo .zip
   - Extraia o arquivo
   - No IntelliJ:
      - Botão direito no projeto → **Open Module Settings**
      - **Libraries** → **+** → **Java**
      - Selecione o `mysql-connector-j-8.x.x.jar`
      - **Apply** → **OK**

4. **Configurar Banco de Dados**
   - Edite `DatabaseConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/sistema_clientes";
   private static final String USER = "root";
   private static final String PASSWORD = "sua_senha_aqui"; // ALTERE AQUI
   

5. **Executar o Projeto**
   - Navegue até `projetoExtra.database.Main.java`
   - Clique direito → **Run 'Main.main()'**
   - Ou use o atalho: **Ctrl+Shift+F10**

### Estrutura do Projeto no IntelliJ
```
POO-AESA/
└── projetoExtra/
├── entities/
│   ├── Customer.java
│   ├── IndividualCustomer.java
│   ├── BusinessCustomer.java
│   ├── Address.java
│   └── Category.java
├── services/
│   ├── CustomerService.java
│   └── APIConsumer.java
├── database/
│   ├── DatabaseConnection.java
│   └── Main.java
├── exceptions/
└── schema_clientes.sql
```

### Solução de Problemas no IntelliJ

**Projeto não é reconhecido:**
- Importe manualmente: **File** → **New** → **Project from Existing Sources**

**Erro "Class not found":**
- Verifique se o MySQL Connector foi adicionado às Libraries
- **File** → **Invalidate Caches and Restart**

**Problemas com Git SSH:**
- Verifique se a chave SSH está configurada no GitHub
- Alternative: Use HTTPS `https://github.com/mnzcarvalho/POO-AESA.git`

**JDK não detectado:**
- **File** → **Project Structure** → **SDKs** → **Add JDK**