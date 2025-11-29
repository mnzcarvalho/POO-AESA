# Sistema de Cadastro de Clientes

## 📖 O que é?
Sistema de cadastro de clientes desenvolvido em Java que permite gerenciar clientes (pessoas físicas e jurídicas) com consulta automática de endereço via API ViaCEP.

## 🎯 Funcionalidades
- ✅ Cadastro de clientes (Pessoa Física e Jurídica)
- ✅ Validação de CPF/CNPJ
- ✅ Consulta automática de endereço via CEP (API ViaCEP)
- ✅ Listagem e busca de clientes
- ✅ Backup e restauração de dados
- ✅ Persistência em banco de dados MySQL
- ✅ Relacionamentos 1:N e N:N

## 🛠️ Tecnologias Utilizadas
- Java 8+
- MySQL
- API ViaCEP
- JDBC para conexão com banco
- Serialização Java

## 📋 Pré-requisitos
- Java JDK 8 ou superior
- MySQL Server
- Conexão com internet (para API ViaCEP)

## 🚀 Como Instalar e Executar

### 1. Configurar Banco de Dados
```sql
-- Execute o script scripts/create_database.sql no MySQL
-- Isso criará o banco 'sistema_clientes' com todas as tabelas necessárias 
```

2. Configurar Conexão com Banco
Edite o arquivo src/database/DatabaseConnection.java:

java  
private static final String URL = "jdbc:mysql://localhost:3306/sistema_clientes";    
private static final String USER = "root"; // Seu usuário MySQL      
private static final String PASSWORD = "sua_senha"; // Sua senha MySQL

3. Compilar e Executar
   bash
# Compilar todos os arquivos
javac -d bin src/*.java src/entities/*.java src/services/*.java src/database/*.java src/exceptions/*.java

# Executar
java -cp bin:lib/mysql-connector-java-8.0.23.jar Main

# 🎮 Como Usar
Menu Principal:
Cadastrar Cliente: Cadastra novo cliente (PF ou PJ) com validação de documento

Listar Clientes: Mostra todos os clientes cadastrados

Buscar Cliente: Busca clientes por nome

Adicionar Endereço: Adiciona endereço consultando API ViaCEP

Fazer Backup: Salva dados em arquivo

Restaurar Backup: Restaura dados de arquivo

Exemplo de Uso:
Cadastre um cliente Pessoa Física informando CPF

Adicione endereço informando apenas CEP e número

O sistema consulta a API e preenche automaticamente logradouro, bairro, cidade e estado

# 🐛 Solução de Problemas
Erro de Conexão com Banco:
Verifique se MySQL está rodando

Confirme usuário e senha no DatabaseConnection.java

Execute o script SQL para criar o banco

Erro de API ViaCEP:
Verifique conexão com internet

Confirme que o CEP é válido (8 dígitos)

