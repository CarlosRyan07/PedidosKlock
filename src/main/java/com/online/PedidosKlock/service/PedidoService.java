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
    private final ClienteRepository clienteRepository;  // Adicionando o ClienteRepository
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
                .orElseThrow(() -> new EntityNotFoundException("Erro: Pedido não encontrado com o id " + id));
    }

    public Pedido criarPedido(Pedido pedido) {
        // Validação para garantir que o pedido contenha itens
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Erro: O pedido deve conter ao menos um item.");
        }

        // Validação para garantir que o cliente esteja informado
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new IllegalArgumentException("Erro: O cliente do pedido não foi informado.");
        }

        // Verificar se o cliente existe no banco de dados
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Erro: Cliente não encontrado com o id " + pedido.getCliente().getId()));

        pedido.setCliente(cliente);

        // Criar uma nova lista de itens, carregando do banco
        List<Item> itensCarregados = new ArrayList<>();
        for (Item itemRequisitado : pedido.getItens()) {
            Item itemBanco = itemRepository.findById(itemRequisitado.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Erro: Item com ID " + itemRequisitado.getId() + " não encontrado"));

            // Atualizando a quantidade com o valor informado no JSON
            itemBanco.setQuantidade(itemRequisitado.getQuantidade());

            itensCarregados.add(itemBanco);
        }
        pedido.setItens(itensCarregados);

        // Calcular totais e verificar estoque
        pedido.calcularTotais();
        pedido.verificarEstoque();
        pedido.definirDataEntrega();

        // Salvar o pedido no banco
        return pedidoRepository.save(pedido);
    }

    public Pedido atualizarPedido(Long id, Pedido pedidoAtualizado) {
        // Procurando o pedido existente
        Pedido pedidoExistente = buscarPorId(id);

        // Atualizando os campos necessários
        pedidoExistente.setItens(pedidoAtualizado.getItens());
        pedidoExistente.calcularTotais();
        pedidoExistente.verificarEstoque();
        pedidoExistente.definirDataEntrega();

        // Salvando as alterações
        return pedidoRepository.save(pedidoExistente);
    }

    public void excluirPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Erro: Pedido não encontrado com o id " + id));

        // Verificar se o pedido contém itens antes de excluí-lo
        if (pedido.getItens() != null && !pedido.getItens().isEmpty()) {
            // Lógica para garantir que a exclusão do pedido não afete o cliente
            // Neste caso, a exclusão do pedido não deve afetar o cliente.
        }

        // Excluindo o pedido
        pedidoRepository.deleteById(id);
    }
}
