# 🏪 Sistema - Loja TECH



## 💡 Conceitos Implementados
## 1. 📊 Estruturas Lógicas

   Onde: Loja.java (métodos de busca), Main.java (menu switch)

    Implementação: Loops for, while, switch, if/else em validações e buscas


## 2. 🏗️ Construtores   
     Onde: Todas as classes em com.loja.model

    Implementação: 2 construtores por classe (vazio e com parâmetros)


## 3. 🔒 Encapsulamento  
   Onde: Todas as classes do pacote model     

    Implementação: Atributos privados + gets/sets com validações


## 4. 🌳 Herança + Abstract   
   Onde:

    Classe abstrata: Pessoa.java

    Classes filhas: Cliente.java, Funcionario.java, Fornecedor.java, Gerente.java

    Implementação: Herança simples e múltipla, métodos abstratos


## 5. 🔄 Polimorfismo + ArrayList + Relacionamentos  
   Onde: Loja.java e Pedido.java

    Implementação:

    ArrayList polimórfico: List<Pessoa>

    Relacionamento 1:N: Loja → Pedidos

    Relacionamento N:N: Pedido → Produtos

    Sobrescrita com @Override


## 6. 💾 Serialização de Objetos  
   Onde: Loja.java e todas as classes do pacote model

    Implementação: Serializable, salvar/carregar em loja.dat


## 7. ⚠️ Exception + Entrada/Saída  
   Onde: Pacote exception e Main.java

    Implementação: 4 exceptions customizadas, try/catch, leitura/gravação de arquivos


# 🎯 Funcionalidades
✅ Cadastro de Clientes, Funcionários, Fornecedores

✅ Gestão de Produtos e Estoque

✅ Realização de Pedidos

✅ Relatórios de Vendas

✅ Persistência de Dados

✅ Validações de CPF e Estoque

# 🔄 Fluxo do Sistema
Inicialização: Carrega dados do arquivo loja.dat

Menu Interativo: Navegação por opções numéricas

Operações: CRUD completo para todas entidades

Persistência: Os dados ficam salvos


# 📊 Dados que Serão Gerados
Clientes Fictícios (8)
Nomes e CPFs válidos

Emails baseados nos nomes

Produtos Fictícios (8)
Eletrônicos variados

Preços entre R$199,99 e R$3.299,99

Estoque entre 25-200 unidades

Pedidos Distribuídos (20-30)
Período: Últimos 6 meses

Distribuição: Pedidos espalhados aleatoriamente

Itens: 1-5 produtos por pedido

Quantidades: 1-3 unidades por item

## 📁 LOCALIZAÇÃO DOS CÓDIGOS

Conceito          Arquivo            Descrição
  ----------------- ------------------ --------------------------------
Encapsulamento    Pessoa.java        Gets/Sets privados
Herança           Cliente.java       extends Pessoa
Polimorfismo      Loja.java          List`<Pessoa>`{=html}
Serialização      Loja.java          salvarDados(), carregarDados()
Exceptions        exception/         4 classes customizadas
Relacionamentos   Pedido.java        List`<ItemPedido>`{=html}
Construtores      Todas as classes   2 construtores cada
Método Abstrato   Pessoa.java        getTipo()

