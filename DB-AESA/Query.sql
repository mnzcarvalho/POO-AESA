
CONSULTAS AVANÇADAS (14)
Consulta 1 -> Total gasto por cliente no período (JOINs)
SELECT clientes.nome AS cliente, SUM(pedidos.total) AS total_gasto
FROM pedidos
JOIN clientes ON pedidos.cliente_id = clientes.id
WHERE clientes.id = 1 AND pedidos.data_pedido BETWEEN '2025-01-01' AND '2025-12-31'
GROUP BY clientes.nome;
-- Consulta 2 -> Top 10 produtos por faturamento (JOINs)
SELECT produtos.id, produtos.nome, SUM(itens_pedido.quantidade * itens_pedido.preco_unitario) AS faturamento
FROM itens_pedido
JOIN produtos ON itens_pedido.produto_id = produtos.id
GROUP BY produtos.id, produtos.nome
ORDER BY faturamento DESC
LIMIT 10;
-- Consulta 3 -> Pedidos via PIX por cidade (JOINs)
SELECT enderecos.cidade, COUNT(pedidos.id) AS pedidos_pix, SUM(pagamentos.valor) AS total_pix
FROM pagamentos
JOIN pedidos ON pagamentos.pedido_id = pedidos.id
JOIN enderecos ON pedidos.endereco_envio_id = enderecos.id
WHERE pagamentos.metodo = 'PIX'
GROUP BY enderecos.cidade;
-- Consulta 4 -> Fornecedores por quantidade fornecida (JOINs)
SELECT fornecedores.nome AS fornecedor, produtos.nome AS produto, SUM(entradas_estoque.quantidade) AS total_recebido
FROM entradas_estoque
JOIN fornecedores ON entradas_estoque.fornecedor_id = fornecedores.id
JOIN produtos ON entradas_estoque.produto_id = produtos.id
GROUP BY fornecedores.nome, produtos.nome
ORDER BY total_recebido DESC;
-- Consulta 5 -> Clientes que mais compraram (JOINs)
SELECT clientes.nome, COUNT(pedidos.id) AS total_pedidos, SUM(pedidos.total) AS total_gasto
FROM clientes
JOIN pedidos ON clientes.id = pedidos.cliente_id
GROUP BY clientes.nome
ORDER BY total_gasto DESC
LIMIT 10;
-- Consulta 6 -> Vendas completas com 5 JOINs (complexa)
SELECT pedidos.id AS pedido, clientes.nome AS cliente, contas_usuarios.usuario AS usuario, pagamentos.metodo AS pagamento, remessas.transportadora AS transportadora, produtos.nome AS produto, itens_pedido.quantidade, pedidos.total
FROM pedidos
JOIN clientes ON pedidos.cliente_id = clientes.id
JOIN contas_usuarios ON clientes.id = contas_usuarios.cliente_id
JOIN pagamentos ON pagamentos.pedido_id = pedidos.id
JOIN remessas ON remessas.pedido_id = pedidos.id
JOIN itens_pedido ON itens_pedido.pedido_id = pedidos.id
JOIN produtos ON itens_pedido.produto_id = produtos.id
WHERE pedidos.data_pedido BETWEEN '2025-06-01' AND '2025-12-31'
LIMIT 200;
-- Consulta 7 -> Produtos sem entradas de estoque (LEFT JOIN)
SELECT produtos.id, produtos.nome, COALESCE(SUM(entradas_estoque.quantidade),0) AS total_estoque
FROM produtos
LEFT JOIN entradas_estoque ON produtos.id = entradas_estoque.produto_id
GROUP BY produtos.id, produtos.nome
HAVING total_estoque = 0;
-- Consulta 8 -> Clientes sem conta criada (LEFT JOIN)
SELECT clientes.id, clientes.nome, clientes.email
FROM clientes
LEFT JOIN contas_usuarios ON clientes.id = contas_usuarios.cliente_id
WHERE contas_usuarios.id IS NULL;
-- Consulta 9 -> Faturamento por categoria (JOINs)
SELECT categorias.nome AS categoria, SUM(itens_pedido.quantidade * itens_pedido.preco_unitario) AS faturamento_categoria
FROM produto_categoria
JOIN categorias ON produto_categoria.categoria_id = categorias.id
JOIN produtos ON produto_categoria.produto_id = produtos.id
JOIN itens_pedido ON produtos.id = itens_pedido.produto_id
JOIN pedidos ON itens_pedido.pedido_id = pedidos.id
GROUP BY categorias.nome
ORDER BY faturamento_categoria DESC;
-- Consulta 10 -> Fornecedores e produtos fornecidos (1:N)
SELECT fornecedores.nome AS fornecedor, produtos.nome AS produto, SUM(entradas_estoque.quantidade) AS qtd_recebida
FROM fornecedores
JOIN entradas_estoque ON fornecedores.id = entradas_estoque.fornecedor_id
JOIN produtos ON entradas_estoque.produto_id = produtos.id
GROUP BY fornecedores.nome, produtos.nome;
-- Consulta 11 -> Pedidos possivelmente atrasados (sem entrega em +7 dias)
SELECT pedidos.id AS pedido, remessas.enviado_em, remessas.entregue_em, DATEDIFF(CURDATE(), remessas.enviado_em) AS dias_desde_envio
FROM remessas
JOIN pedidos ON remessas.pedido_id = pedidos.id
WHERE remessas.entregue_em IS NULL AND DATEDIFF(CURDATE(), remessas.enviado_em) > 7;
-- Consulta 12 -> Itens mais vendidos por produto e categoria (JOINs)
SELECT produtos.id, produtos.nome, categorias.nome AS categoria, SUM(itens_pedido.quantidade) AS unidades_vendidas
FROM itens_pedido
JOIN produtos ON itens_pedido.produto_id = produtos.id
JOIN produto_categoria ON produtos.id = produto_categoria.produto_id
JOIN categorias ON produto_categoria.categoria_id = categorias.id
GROUP BY produtos.id, produtos.nome, categorias.nome
ORDER BY unidades_vendidas DESC
LIMIT 20;
-- Consulta 13 -> Performance dos clientes (RFM simplificado)
SELECT clientes.id, clientes.nome,
       COUNT(pedidos.id) AS recencia_contagem,
       MAX(pedidos.data_pedido) AS ultima_compra,
       SUM(pedidos.total) AS gasto_total
FROM clientes
LEFT JOIN pedidos ON clientes.id = pedidos.cliente_id
GROUP BY clientes.id, clientes.nome
ORDER BY gasto_total DESC
LIMIT 50;
-- Consulta 14 -> Consulta complexa com 6 JOINs (muitos joins)
SELECT o.id AS pedido, c.nome AS cliente, ua.usuario AS usuario, p.nome AS produto, cat.nome AS categoria, f.nome AS fornecedor, pay.metodo AS pagamento, r.transportadora AS transportadora, o.total AS total_pedido
FROM pedidos o
JOIN clientes c ON o.cliente_id = c.id
JOIN contas_usuarios ua ON c.id = ua.cliente_id
JOIN itens_pedido ip ON o.id = ip.pedido_id
JOIN produtos p ON ip.produto_id = p.id
JOIN produto_categoria pc ON p.id = pc.produto_id
JOIN categorias cat ON pc.categoria_id = cat.id
JOIN entradas_estoque ee ON p.id = ee.produto_id
JOIN fornecedores f ON ee.fornecedor_id = f.id
LEFT JOIN pagamentos pay ON pay.pedido_id = o.id
LEFT JOIN remessas r ON r.pedido_id = o.id
WHERE o.data_pedido BETWEEN '2025-01-01' AND '2025-12-31'
LIMIT 200;
