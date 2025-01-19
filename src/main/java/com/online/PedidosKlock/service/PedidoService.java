package com.online.PedidosKlock.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import com.online.PedidosKlock.model.Cliente;
import com.online.PedidosKlock.model.Item;
import com.online.PedidosKlock.model.Pedido;
import com.online.PedidosKlock.repository.ClienteRepository;
import com.online.PedidosKlock.repository.PedidoRepository;
import com.online.PedidosKlock.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;  // Adicione esta linha
    private final ItemRepository itemRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, ItemRepository itemRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.itemRepository = itemRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    }

    public Pedido criarPedido(Pedido pedido) {
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item.");
        }

        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new IllegalArgumentException("O cliente do pedido não foi informado.");
        }

        // Verificar e carregar o cliente do banco
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        pedido.setCliente(cliente);

        // Criar uma nova lista de itens carregados do banco
        List<Item> itensCarregados = new ArrayList<>();
        for (Item itemRequisitado : pedido.getItens()) {
            // Buscar o item no banco pelo ID
            Item itemBanco = itemRepository.findById(itemRequisitado.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Item com ID " + itemRequisitado.getId() + " não encontrado"));

            // Atualizar a quantidade com o valor enviado no JSON
            itemBanco.setQuantidade(itemRequisitado.getQuantidade());

            itensCarregados.add(itemBanco);
        }
        pedido.setItens(itensCarregados);

        // Calcular valores do pedido
        pedido.calcularTotais(); // Calcula total e totalComDesconto
        pedido.verificarEstoque(); // Verifica se todos os itens têm estoque
        pedido.definirDataEntrega(); // Define a data de entrega

        // Salvar o pedido no banco
        return pedidoRepository.save(pedido);
    }

    

    public Pedido atualizarPedido(Long id, Pedido pedidoAtualizado) {
        Pedido pedidoExistente = buscarPorId(id);

        // Atualizar os campos necessários
        pedidoExistente.setItens(pedidoAtualizado.getItens());
        pedidoExistente.calcularTotais();
        pedidoExistente.verificarEstoque();
        pedidoExistente.definirDataEntrega();

        return pedidoRepository.save(pedidoExistente);
    }

    public void excluirPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
    
        // Verificar se o pedido contém itens e se a exclusão do pedido não afeta o cliente
        if (pedido.getItens() != null && !pedido.getItens().isEmpty()) {
            // Lógica para garantir que a exclusão do pedido não afete o cliente
            // No caso de pedido com itens, ele é excluído sem afetar o cliente
        }
    
       
        pedidoRepository.deleteById(id);
    }
    
}
