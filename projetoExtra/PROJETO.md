📋 Estrutura do Projeto
🎯 Funcionalidades Principais
Cadastrar, editar, listar e excluir clientes

Consulta automática de endereço via ViaCEP

Validação de CPF

Categorização de clientes

🏗️ Estrutura de Classes
Classes Principais:
Cliente (classe pai)

ClientePessoaFisica (herda de Cliente)

ClientePessoaJuridica (herda de Cliente)

Endereco

Categoria

Relacionamentos:
1:N - Cliente tem vários Endereco (entrega, cobrança, etc.)

N:N - Cliente pertence a várias Categoria, Categoria tem vários Cliente

⚙️ Como Atende Cada Requisito
Estruturas Lógicas
Loops para listar clientes, filtrar por categoria

If/else para validações de CPF/CNPJ

Switch para menu principal

Construtores
Cada classe com construtor vazio e com parâmetros

Encapsulamento
Todos atributos privados

Setters com validação (CPF, email, CEP)

Herança + Abstract
Cliente como classe abstrata

Métodos abstratos em Cliente implementados nas filhas

Polimorfismo + ArrayList
ArrayList<Cliente> contendo ClientePessoaFisica e ClientePessoaJuridica

Sobrescrita de Métodos
@Override no método de validação de documento

Serialização
Backup dos dados dos clientes

Exception Customizadas
CPFInvalidoException

CEPInvalidoException

ClienteNaoEncontradoException

📊 Estrutura do Banco de Dados
sql
-- Tabelas: clientes, enderecos, categorias, cliente_categoria
-- Relacionamentos 1:N e N:N implementados
🎮 Interface
Menu no terminal em português

Opções: Cadastrar, Listar, Editar, Excluir, Buscar por CEP

🔗 API Externa
ViaCEP: Para preenchimento automático do endereço

📁 Estrutura de Arquivos
text
/src
/entities
Cliente.java (abstract)
ClientePessoaFisica.java
ClientePessoaJuridica.java
Endereco.java
Categoria.java
/exceptions
CPFInvalidoException.java
CEPInvalidoException.java
/services
ClienteService.java
APIConsumer.java
/database
DatabaseConnection.java
Main.java
/scripts
create_database.sql
README.md