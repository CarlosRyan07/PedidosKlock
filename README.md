# Klok Pedido Service

Este projeto é uma refatoração do serviço de pedidos originalmente fornecido. Ele foi projetado para melhorar a legibilidade, manutenção e eficiência, garantindo que a lógica de negócios seja clara e bem organizada.

A Classe que me foi apresetanda para refatoração foi essa:

```java
  public class PedidoService {

    public void processarPedidos(List<Pedido> pedidos) {
        for (Pedido pedido : pedidos) {
            double total = 0;

            for (Item item : pedido.getItems()) {
                total += item.getPreco() * item.getQuantidade();
            }

            pedido.setTotal(total);

            if (pedido.getCliente().isVip()) {
                total *= 0.9;
            }

            pedido.setTotalComDesconto(total);

            boolean emEstoque = true;
            for (Item item : pedido.getItems()) {
                if (item.getQuantidade() > item.getEstoque()) {
                    emEstoque = false;
                    break;
                }
            }
            pedido.setEmEstoque(emEstoque);

            if (emEstoque) {
                pedido.setDataEntrega(LocalDate.now().plusDays(3));
            } else {
                pedido.setDataEntrega(null);
            }

            if (emEstoque) {
                enviarNotificacao(pedido.getCliente().getEmail(), "Seu pedido será entregue em breve.");
            } else {
                enviarNotificacao(pedido.getCliente().getEmail(), "Um ou mais itens do seu pedido estão fora de estoque.");
            }
        }
    }

    private void enviarNotificacao(String email, String mensagem) {
        System.out.println("Enviando e-mail para " + email + ": " + mensagem);
    }
}
```
e abaixo como eu fiz isso.
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
  Rode a classe `PedidosKlockApplication` para iniciar o serviço.
  ```

4. **Acesse a API:**
  O serviço estará disponível em `http://localhost:8080`.

5. **Utilizei o Postman:**
  Importe a coleção de requests do Postman disponível no repositório para testar os endpoints da API.

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
