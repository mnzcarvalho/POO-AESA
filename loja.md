# Cenários de Teste - Sistema de Loja POO

## 📊 CENÁRIO 1: Cadastro Completo — ✅ PRONTO
**Como demonstrar:**  
Menu → Opção 1 (Cadastrar Cliente)

```
Nome: Ana Teste
CPF: 12345678901
Email: ana@teste.com
```

**Encapsulamento:**  
`Pessoa.java` — atributos *private* + métodos *public* get/set

**Validação:**  
`Pessoa.setCpf()` → `ValidacaoCPF.validar()`  
Testar CPF inválido: `"123"` → `CPFInvalidoException`

**Serialização:**  
`Loja.salvarDados()` → **loja.dat**  
Objeto salvo → verificar arquivo gerado

---

## 📊 CENÁRIO 2: Herança — ✅ PRONTO
**Como demonstrar:**  
Classes filhas:

- Cliente  
- Funcionario  
- Fornecedor  
- Gerente  

**ArrayList polimórfico:**  
`Loja.java` — `List<Pessoa> pessoas`

**Hierarquia:**
```
Pessoa (abstract)
├── Cliente
├── Funcionario
│   └── Gerente
└── Fornecedor
```

**Polimorfismo:** `pessoa.getTipo()` retorna resultados diferentes.

---

## 📊 CENÁRIO 3: Busca e Filtro — ✅ PRONTO
**Como demonstrar:**

- Carregar dados: `Loja.carregarDados()`
- Loop for: `Loja.buscarCliente()`
- Filtro `instanceof`: verifica se é `Cliente`
- Ordenação: `Loja.listarPedidos()` com `sort()`

Menu recomendado → Opção **6** (Pedidos ordenados)

---

## 📊 CENÁRIO 4: Relacionamento entre Classes — ✅ PRONTO
**Como demonstrar:**

- Relação **1:N** → `Loja → List<Pedido>`
- Relação **N:N** → `Pedido → List<ItemPedido> → Produto`

**Conexões:**  
Cliente → Pedido → Itens → Produtos

**Implementação:**  
`ItemPedido` como tabela associativa

---

## 📊 CENÁRIO 5: Exceptions e Tratamento de Erros — ✅ PRONTO
**Como demonstrar:**

Forçar erro no menu:

```
CPF: 123 (inválido)
```

- Exception customizada: `CPFInvalidoException`
- `try/catch`: `Main.cadastrarCliente()`
- Outras exceptions:
  - `EstoqueInsuficienteException`
  - `ClienteNaoEncontradoException`

---

## 📊 CENÁRIO 6: Leitura de Arquivo — ✅ PRONTO
**Arquivo CSV:** `produtos.csv`

Exemplo:
```
Nome;Preço;Estoque
Notebook Gamer;4500.00;15
```

**Leitura:** `ImportadorCSV.importarProdutosCSV()`

Processo:
- `BufferedReader`
- `split(";")`
- `Double.parseDouble()`

Criação: `new Produto(nome, preco, estoque)`  
Código no pacote `pooP2.util.ImportadorCSV`

---

## 📊 CENÁRIO 7: Relatório Completo — ✅ PRONTO
**Como demonstrar:**

- Gerar relatório: Menu → Opção **8**
- Filtrar por mês/ano
- Ordenar por data
- Gravar arquivo: Menu → Opção **11**

`Loja.salvarRelatorioMensal("relatorio.txt")`  
Arquivo gerado: **relatorio.txt**

---

## 📊 CENÁRIO 8: Atualização e Persistência — ✅ PRONTO
**Como demonstrar:**

- Carregar: `Loja.carregarDados()`
- Buscar e alterar produto:

```java
produto.setPreco(999.99); // Validação > 0
```

- Salvar novamente: `Loja.salvarDados()`
- Reiniciar e validar persistência

---

## 📊 CENÁRIO 9: Menu Interativo — ✅ PRONTO
**Como demonstrar:**

Executar: `Main.main()` → `exibirMenu()`

Três navegações sugeridas:
- Opção 9 → dados fictícios  
- Opção 4 → listar clientes  
- Opção 8 → relatório mensal  

Fluxo geral: Inicialização → Menu → Processamento → Persistência

---

## 📊 CENÁRIO 10: Construtores — ✅ PRONTO
**Como demonstrar:**

- Construtor vazio: `new Produto()` — usado na serialização  
- Construtor completo: `new Produto("Tablet", 1299.99, 25)`

Diferenças: atributos já preenchidos vs. uso de setters  
Casos de uso: importação, cadastro e serialização

---

## 📊 CENÁRIO 11: Método Abstrato — ✅ PRONTO
**Como demonstrar:**

Classe abstrata: `Pessoa.java` — `abstract String getTipo()`

Implementações:

- Cliente → `"Cliente"`
- Funcionario → `"Funcionario - " + cargo`
- Gerente → `"Gerente do " + departamento`

Polimorfismo: mesma chamada → comportamentos diferentes
