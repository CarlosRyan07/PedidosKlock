# Klok Pedido Service

Este projeto é uma refatoração do serviço de pedidos originalmente fornecido. Ele foi projetado para melhorar a legibilidade, manutenção e eficiência, garantindo que a lógica de negócios seja clara e bem organizada.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.x**
- **Maven**
- **JUnit 5** (para testes unitários)
- **Mockito** (para mocks nos testes)

## Como Executar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone <url-do-repositorio>
   cd klok-pedido-service
   ```

2. **Configure o projeto:**
   Certifique-se de que você tem o JDK 17+ e o Maven instalados.

3. **Execute o projeto:**
   ```bash
   mvn spring-boot:run
   ```

4. **Acesse a API:**
   O serviço estará disponível em `http://localhost:8080`.

## Endpoints da API

### Clientes

- **POST /clientes**: Criar um cliente
- **GET /clientes**: Listar todos os clientes
- **GET /clientes/{id}**: Buscar cliente por ID
- **PUT /clientes/{id}**: Atualizar cliente
- **DELETE /clientes/{id}**: Excluir cliente

### Pedidos

- **POST /pedidos**: Criar um pedido
- **GET /pedidos**: Listar todos os pedidos
- **GET /pedidos/{id}**: Buscar pedido por ID
- **PUT /pedidos/{id}**: Atualizar pedido
- **DELETE /pedidos/{id}**: Excluir pedido

### Itens

- **POST /itens**: Criar um item
- **GET /itens**: Listar todos os itens
- **GET /itens/{id}**: Buscar item por ID
- **PUT /itens/{id}**: Atualizar item
- **DELETE /itens/{id}**: Excluir item

## Exemplos de Uso

### Criar Cliente

Requisição:
```json
POST /clientes
Content-Type: application/json

{
  "nome": "João da Silva",
  "email": "joao.silva@email.com",
  "vip": true
}
```

Resposta:
```json
{
  "id": 1,
  "nome": "João da Silva",
  "email": "joao.silva@email.com",
  "vip": true
}
```

### Criar Item

Requisição:
```json
POST /itens
Content-Type: application/json

{
  "nome": "Teclado Mecânico",
  "preco": 300.0,
  "quantidade": 10,
  "estoque": 20
}
```

Resposta:
```json
{
  "id": 1,
  "nome": "Teclado Mecânico",
  "preco": 300.0,
  "quantidade": 10,
  "estoque": 20
}
```

### Criar Pedido

Requisição:
```json
POST /pedidos
Content-Type: application/json

{
  "clienteId": 1,
  "items": [
    {
      "itemId": 1,
      "quantidade": 2
    }
  ]
}
```

Resposta:
```json
[
    {"id": 1,
        "cliente": {
            "id": 1,
            "nome": "Ryan",
            "email": "ryan@email.com",
            "vip": true
        },
        "itens": [
            {
                "id": 1,
                "nome": "Teclado Mecânico",
                "preco": 300.0,
                "quantidade": 10,
                "estoque": 20
            }
        ],

        "total": 600.0,
        "totalComDesconto": 540.0,
        "emEstoque": true,
        "dataCompra": "2025-01-19",
        "dataEntrega": "2025-01-23"
    }
]    
```

## Testes

Os testes foram implementados utilizando JUnit 5 e Mockito para garantir a confiabilidade e robustez do código.

### Cobertura de Testes

- **ClienteServiceTest**: Testes para criação, atualização, exclusão e listagem de clientes.
- **PedidoServiceTest**: Testes para a lógica de processamento de pedidos.
- **ItemServiceTest**: Testes para criação, atualização, exclusão e listagem de itens.

## Estrutura do Projeto

```plaintext
src/main/java
├── com.online.PedidosKlock
│   ├── controller
│   │   ├── ClienteController.java
│   │   ├── PedidoController.java
│   │   └── ItemController.java
│   ├── model
│   │   ├── Cliente.java
│   │   ├── Pedido.java
│   │   └── Item.java
│   ├── repository
│   │   ├── ClienteRepository.java
│   │   ├── PedidoRepository.java
│   │   └── ItemRepository.java
│   └── service
│       ├── ClienteService.java
│       ├── PedidoService.java
│       └── ItemService.java
└── resources
    ├── application.properties
    └── data.sql
```
