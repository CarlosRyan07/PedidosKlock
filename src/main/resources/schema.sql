-- Tabela de Clientes
CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE, -- Garantir unicidade do email
    is_vip BOOLEAN NOT NULL -- Campo para identificar se o cliente é VIP
);

-- Tabela de Itens
CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL, -- Limitar tamanho do nome
    preco DOUBLE PRECISION NOT NULL CHECK (preco >= 0), -- Preço não pode ser negativo
    quantidade INT NOT NULL CHECK (quantidade >= 0), -- Quantidade não pode ser negativa
    estoque INT NOT NULL CHECK (estoque >= 0) -- Estoque não pode ser negativo
);

-- Tabela de Pedidos
CREATE TABLE pedido (
    id SERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    total DOUBLE PRECISION NOT NULL DEFAULT 0 CHECK (total >= 0), -- Valor total não pode ser negativo
    total_com_desconto DOUBLE PRECISION NOT NULL DEFAULT 0 CHECK (total_com_desconto >= 0), -- Valor com desconto
    em_estoque BOOLEAN NOT NULL DEFAULT TRUE, -- Valor padrão TRUE
    data_entrega DATE,
    CONSTRAINT fk_cliente_pedido FOREIGN KEY (cliente_id) 
        REFERENCES cliente(id)
        ON DELETE SET NULL -- Quando o pedido for deletado, o cliente_id será setado como NULL
);

-- Tabela de Associação de Pedido com Itens
CREATE TABLE pedido_items (
    pedido_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    PRIMARY KEY (pedido_id, item_id),
    CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) 
        REFERENCES pedido(id) 
        ON DELETE CASCADE, -- Excluir itens quando o pedido for deletado
    CONSTRAINT fk_item FOREIGN KEY (item_id) 
        REFERENCES item(id) 
        ON DELETE CASCADE -- Excluir itens associados ao pedido
);

