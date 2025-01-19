CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE, -- Garantir unicidade do email
    is_vip BOOLEAN NOT NULL
);

CREATE TABLE item (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL, -- Limitar tamanho do nome
    preco DOUBLE PRECISION NOT NULL CHECK (preco >= 0), -- Preço não pode ser negativo
    quantidade INT NOT NULL CHECK (quantidade >= 0), -- Quantidade não pode ser negativa
    estoque INT NOT NULL CHECK (estoque >= 0) -- Estoque não pode ser negativo
);

CREATE TABLE pedido (
    id SERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES cliente(id) ON DELETE RESTRICT, -- Restringir exclusão de cliente associado
    total DOUBLE PRECISION NOT NULL DEFAULT 0 CHECK (total >= 0), -- Valor total não pode ser negativo
    total_com_desconto DOUBLE PRECISION NOT NULL DEFAULT 0 CHECK (total_com_desconto >= 0), -- Valor com desconto
    em_estoque BOOLEAN NOT NULL DEFAULT TRUE, -- Valor padrão TRUE
    data_entrega DATE
);

CREATE TABLE pedido_items (
    pedido_id BIGINT NOT NULL REFERENCES pedido(id) ON DELETE CASCADE, -- Excluir associações ao deletar pedido
    item_id BIGINT NOT NULL REFERENCES item(id) ON DELETE CASCADE, -- Excluir associações ao deletar item
    PRIMARY KEY (pedido_id, item_id)
);
